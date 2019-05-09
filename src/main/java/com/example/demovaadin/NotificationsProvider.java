package com.example.demovaadin;


import io.reactivex.Observable;

public interface NotificationsProvider {
    Observable<String> getNotifications();
}
