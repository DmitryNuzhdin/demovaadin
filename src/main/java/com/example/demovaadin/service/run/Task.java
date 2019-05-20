package com.example.demovaadin.service.run;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    private String user;
    private String script;
    private boolean commit;
    private ReplaySubject<String> result = ReplaySubject.create();
    private BehaviorSubject<TaskStatus> status= BehaviorSubject.create();
    private Map<String,String> attributes = new HashMap<>();

    private static Pattern MY_PATTERN = Pattern.compile("(?s)(?<=/\\*).*?(?=\\*/)");

    public Task(String user, String script, boolean commit) {
        this.user = user;
        this.script = script;
        this.commit = commit;
        status.onNext(TaskStatus.CREATED);
    }

    public String getUser() {
        return user;
    }

    public String getScript() {
        return script;
    }

    public Observable<String> getResult() {
        return result;
    }

    public Observable<TaskStatus> getStatus() {
        return status;
    }

    public boolean isCommit() {
        return commit;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    Subject<String> resultChanger(){
        return result;
    }

    Subject<TaskStatus> statusChanger() {
        return status;
    }

    public void initAttributes(){
        Matcher m = MY_PATTERN.matcher(script);
        if (m.find()) {
            String[] lines = m.group(0).replace("\r","").split("\n");
            for (String line : lines) {
                System.out.println(line.trim());
            }
        }

    }

}
