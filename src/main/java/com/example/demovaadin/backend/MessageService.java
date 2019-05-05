package com.example.demovaadin.backend;

import io.reactivex.Observable;

import java.util.List;


public interface MessageService {
    Observable<List<Message>> messages();
    void sendMessage(Message message);
}
