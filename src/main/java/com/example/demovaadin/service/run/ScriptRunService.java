package com.example.demovaadin.service.run;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;
import org.springframework.stereotype.Component;

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
            task.resultChanger().onNext("Task started, user = "+task.getUser());
            task.resultChanger().onNext("Script = "+task.getScript());
            Thread.sleep(10000);
            task.resultChanger().onNext("Done");
            task.resultChanger().onComplete();
            task.statusChanger().onNext(TaskStatus.SUCCESSFUL);
        });
    }

    public void runScript(String username, String script){
        tasks.onNext(new Task(username,script));
    }

    public Observable<Task> getTasks() {
        return tasks;
    }
}

