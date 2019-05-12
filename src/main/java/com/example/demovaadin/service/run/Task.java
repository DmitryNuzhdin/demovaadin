package com.example.demovaadin.service.run;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

public class Task {
    private String user;
    private String script;
    private ReplaySubject<Object> result = ReplaySubject.create();
    private BehaviorSubject<TaskStatus> status= BehaviorSubject.create();

    public Task(String user, String script) {
        this.user = user;
        this.script = script;
        status.onNext(TaskStatus.CREATED);
    }

    public String getUser() {
        return user;
    }

    public String getScript() {
        return script;
    }

    public Observable<Object> getResult() {
        return result;
    }

    public Observable<TaskStatus> getStatus() {
        return status;
    }

    Subject<Object> resultChanger(){
        return result;
    }

    Subject<TaskStatus> statusChanger() {
        return status;
    }
}
