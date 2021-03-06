package com.example.demovaadin.ui.auth;

import com.example.demovaadin.service.auth.Authentication;
import com.example.demovaadin.service.auth.AuthenticationProvider;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import org.springframework.beans.factory.DisposableBean;

@SpringComponent
@VaadinSessionScope
public class AuthenticationHolder implements DisposableBean{
    private AuthenticationProvider authenticationProvider;
    private BehaviorSubject<Authentication> currentAuth = BehaviorSubject.create();
    private CompositeDisposable subscription = new CompositeDisposable();

    public AuthenticationHolder(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        currentAuth.onNext(Authentication.emptyAuthentication());
    }

    public Observable<Authentication> getAuthentication(){
        return currentAuth;
    }

    public Observable<Boolean> hasRole(String role){
        return currentAuth.map(a->a.getRoles().contains(role));
    }

    public void authenticate(String username, String password){
        subscription.clear();
        Observable<Authentication> ans = authenticationProvider.authenticate(username,password);
        subscription.add(ans.subscribe(currentAuth::onNext));
    }

    public void logOut(){
        //authenticationProvider.logOut(currentAuth.blockingFirst().getUsername());
    }

    @Override
    public void destroy() throws Exception {
        subscription.dispose();
        currentAuth.onComplete();
    }
}
