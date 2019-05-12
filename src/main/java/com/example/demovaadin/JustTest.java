package com.example.demovaadin;


import io.reactivex.Observable;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JustTest {
    public static void main(String[] args){
        Observable<String> o1 = Observable.just("q","w","e");
        Observable<Long> o2 = Observable.interval(1, TimeUnit.SECONDS);

        Observable
            .zip(o1,o2,(s, aLong) -> s)
            .take(2)
            .map(s -> s.toUpperCase())
            .repeat()
            .subscribe(System.out::println);


        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
