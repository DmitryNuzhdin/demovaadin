package com.example.demovaadin.ui.common;

import com.vaadin.flow.shared.Registration;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import java.util.HashSet;
import java.util.Set;

public class UniversalDisposable {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Set<Registration> registrations = new HashSet<>();

    public void add(Disposable disposable){
        compositeDisposable.add(disposable);
    }

    public void add(Registration registration){
        registrations.add(registration);
    }

    public void dispose(){
        compositeDisposable.clear();
        registrations.forEach(Registration::remove);
    }
}
