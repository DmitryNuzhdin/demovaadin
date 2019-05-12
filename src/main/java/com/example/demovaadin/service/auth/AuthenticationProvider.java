package com.example.demovaadin.service.auth;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class AuthenticationProvider{
    private Map<String,UserEntry> users = new HashMap<>();

    public AuthenticationProvider(){

        List<Role> adminAuthorities = new ArrayList<>();
        adminAuthorities.add(Role.ROLE_RUN_SCRIPT);
        adminAuthorities.add(Role.ROLE_SEE_STATUS);
        adminAuthorities.add(Role.ROLE_SEND_MESSAGE);
        users.put("admin", new UserEntry(new Authentication("admin","admin",adminAuthorities)));
    }

    public Observable<Authentication> authenticate(String username, String password){
        UserEntry ans = users.get(username);
        if (ans!=null){
            return
                ans.getSource()
                    .takeUntil(a-> !a.checkPassword(password))
                    .map(a ->  a.checkPassword(password) ? a : Authentication.emptyAuthentication());
        } else return Observable.just(Authentication.emptyAuthentication());
    }
}

class UserEntry{
    private Subject<Authentication> user;
    private Disposable handlerSubscription;
    private Subject<Authentication> logoutHandler;
    private Observable<Authentication> source;

    public UserEntry(Authentication authentication) {
        user = BehaviorSubject.create();
        logoutHandler = BehaviorSubject.create();
        handlerSubscription=user.subscribe(logoutHandler::onNext);
        source = logoutHandler
            .doOnLifecycle(disposable -> System.out.println("loggen in"), () -> System.out.println("loggen out"))
            .replay(1)
            .refCount();
        user.onNext(authentication);
    }

    public void updateUser(Authentication authentication){
        user.onNext(authentication);
    }

    public void logOut(){
        handlerSubscription.dispose();
        logoutHandler.onNext(Authentication.emptyAuthentication());
        logoutHandler.onComplete();

        logoutHandler = BehaviorSubject.create();
        handlerSubscription=user.subscribe(logoutHandler::onNext);
        source = logoutHandler
            .doOnLifecycle(disposable -> System.out.println("loggen in"), () -> System.out.println("loggen out"))
            .replay(1)
            .refCount();
    }

    public Observable<Authentication> getSource() {
        return source;
    }
}