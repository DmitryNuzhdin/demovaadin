package com.example.demovaadin.authService;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class AuthenticationProvider{
    private Map<String,Subject<Authentication>> users = new HashMap<>();

    public AuthenticationProvider(){
        List<String> adminAuthorities = new ArrayList<>();
        adminAuthorities.add("ROLE_ADMIN");
        BehaviorSubject<Authentication> bs = BehaviorSubject.create();
        bs.onNext(new Authentication("admin","admin",adminAuthorities,false));
        users.put("admin", bs);
    }

    public Observable<Authentication> authenticate(String username, String password){
        Subject<Authentication> ans = users.get(username);
        if (ans!=null){
            ans
                .take(1)
                .filter(a->a.checkPassword(password))
                .subscribe(a-> ans.onNext(Authentication.builder(a).authorized(true).build()));
            return
                ans
                    .takeUntil(a-> !a.checkPassword(password) || !a.isAuthorized())
                    .map(a ->  a.checkPassword(password) ? a : Authentication.emptyAuthentication());
        } else return Observable.just(Authentication.emptyAuthentication());
    }

    public void logOut(String username){
        Subject<Authentication> ans = users.get(username);
        if (ans!=null){
            ans
                .take(1)
                .subscribe(a-> ans.onNext(Authentication.builder(a).authorized(false).build()));
        }
    }
}
