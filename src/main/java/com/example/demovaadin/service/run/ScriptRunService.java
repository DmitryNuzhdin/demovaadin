package com.example.demovaadin.service.run;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ScriptRunService {
    private Scheduler scheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    //private ExecutorService executor = Executors.newSingleThreadExecutor();

    private ReplaySubject<Task> tasks = ReplaySubject.create();

    public ScriptRunService() {
        tasks.observeOn(scheduler).subscribe(task -> {
            task.statusChanger().onNext(TaskStatus.IN_PROGRESS);
            runScript(task);
            task.resultChanger().onComplete();
            task.statusChanger().onNext(TaskStatus.SUCCESSFUL);
        });
    }
    
    private void runScript(Task task){
        String script = task.getScript();
        ReactiveStringWriter outWriter = new ReactiveStringWriter(s -> task.resultChanger().onNext(s));
        Binding binding = new Binding();
        binding.setProperty("out", outWriter);
        CompilerConfiguration conf = new CompilerConfiguration();
        //conf.setScriptBaseClass("ru.atsenergo.rd2.service.dsl_engine.DslBaseClass");
    
        GroovyShell shell = new GroovyShell(this.getClass().getClassLoader(), binding, conf);
        shell.run(script, "script.groovy", new String[]{});
        try {
            outWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runScript(String username, String script){
        tasks.onNext(new Task(username,script, false));
    }

    public Observable<Task> getTasks() {
        return tasks;
    }
}

