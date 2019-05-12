package com.example.demovaadin.service.message;

import io.reactivex.Observable;

import java.util.List;


public interface MessageService {
    Observable<List<Message>> messages();
    void sendMessage(Message message);
}
