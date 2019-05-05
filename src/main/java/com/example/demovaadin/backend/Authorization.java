package com.example.demovaadin.backend;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

import java.util.Random;

public class Authorization {
    private static final Random random = new Random();

    private long token;
    private PublishSubject<String> errorMessage = PublishSubject.create();
    private BehaviorSubject<String> user = BehaviorSubject.create();

    private Authorization(long token) {
        this.token = token;
        user.onNext("");
    }

    public static final Authorization create(){
        return new Authorization(random.nextLong());
    }

    public Observable<String> user(){
        return user;
    }

    public Observable<String> errorMessage(){
        return errorMessage;
    }

    public long getToken() {
        return token;
    }

    @Override
    public int hashCode() {
        return (int) (token ^ (token >>> 32));
    }

    protected void sendError(String error){
        errorMessage.onNext(error);
    }

    protected void setUser(String user){
        this.user.onNext(user);
    }
}
