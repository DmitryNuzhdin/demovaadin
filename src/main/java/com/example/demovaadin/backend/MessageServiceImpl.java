package com.example.demovaadin.backend;

import com.vaadin.flow.spring.annotation.SpringComponent;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import org.springframework.web.context.annotation.ApplicationScope;

import java.util.Collections;
import java.util.List;

@SpringComponent
@ApplicationScope
public class MessageServiceImpl implements MessageService {
    private BehaviorSubject<List<Message>> messageFlow = BehaviorSubject.create();
    private PublishSubject<Message> messagesReceiver = PublishSubject.create();

    public MessageServiceImpl() {
        messagesReceiver
            .observeOn(Schedulers.newThread())
            .withLatestFrom(messageFlow, (Message message, List<Message> messages) -> Observable
                .fromIterable(messages)
                .concatWith(Observable.just(message))
                .takeLast(10)
                .toList()
                .map(Collections::unmodifiableList))
            .subscribe(listSingle ->  messageFlow.onNext(listSingle.blockingGet()));
        messageFlow.onNext(Collections.unmodifiableList(Collections.emptyList()));
    }

    @Override
    public Observable<List<Message>> messages() {
        return messageFlow;
    }

    @Override
    public void sendMessage(Message message) {
        messagesReceiver.onNext(message);
    }
}
