package com.example.demovaadin.ui.chat;

import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.service.auth.Authentication;
import com.example.demovaadin.service.auth.AuthenticationProvider;
import com.example.demovaadin.service.auth.Role;
import com.example.demovaadin.service.message.Message;
import com.example.demovaadin.service.message.MessageService;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@SpringComponent
@UIScope
public class ChatUI extends VerticalLayout implements UIWidget {
    //Injects
    AuthenticationHolder authenticationHolder;
    AuthenticationProvider authenticationProvider;
    MessageService messageService;
    //Disposables
    private CompositeDisposable disposables = new CompositeDisposable();
    private Set<Registration> registrations = new HashSet<>();
    //UI components
    private VerticalLayout messages = new VerticalLayout();
    private VerticalLayout usersList = new VerticalLayout();

    private Tab chatTab = new Tab("Chat");
    private Tab usersTab = new Tab("Users");
    private Tabs tabs = new Tabs(chatTab, usersTab);
    private VerticalLayout tabsContent = new VerticalLayout();

    private Label myLogin = new Label("");
    private TextField sendText = new TextField();
    private Button sendButton = new Button(VaadinIcon.ARROW_RIGHT.create());
    private HorizontalLayout sendLayout = new HorizontalLayout(myLogin, sendText,sendButton);
    //UI Logic
    private Consumer<List<Message>> REFRESH_MESSAGE_LIST = ml -> access(()->{
        messages.removeAll();
        ml.forEach(m->messages.add(new Label(m.getUser()+": "+m.getMessage())));
    });
    private Consumer<Authentication> SHOW_HIDE_SEND_LAYOUT = a -> access(() -> {
        if (a.getRoles().contains(Role.ROLE_SEND_MESSAGE))
            add(sendLayout);
        else remove(sendLayout);
    });
    private Consumer<Set<String>> REFRESH_USERS_LIST = ss -> access(() -> {
        usersList.removeAll();
        ss.forEach(s->usersList.add(new Label(s)));
    });


    public ChatUI(AuthenticationHolder authenticationHolder, AuthenticationProvider authenticationProvider, MessageService messageService) {
        this.authenticationHolder = authenticationHolder;
        this.authenticationProvider = authenticationProvider;
        this.messageService = messageService;
    }

    private void configureLayout(){
        removeAll();
        messages.setSizeFull();
        usersList.setSizeFull();
        tabsContent.setSizeFull();
        tabsContent.add(messages);
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.setWidthFull();
        messages.getElement().getStyle().set("overflow", "auto");
        this.setSizeFull();
        this.setPadding(true);
        sendText.setWidthFull();
        sendLayout.setWidthFull();
        usersList.getElement().getStyle().set("overflow", "auto");
        add(tabs);
        add(tabsContent);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        configureLayout();
        createLogic();
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposables.clear();
        registrations.forEach(Registration::remove);
        super.onDetach(detachEvent);
    }

    private void createLogic() {
        disposables.add(messageService.messages().subscribe(REFRESH_MESSAGE_LIST));
        disposables.add(authenticationHolder.getAuthentication().subscribe(SHOW_HIDE_SEND_LAYOUT));
        //disposables.add(authenticationProvider.usersOnline().subscribe(REFRESH_USERS_LIST));
        registrations.add(sendButton.addClickListener(buttonClickEvent -> {
            String username = authenticationHolder.getAuthentication().blockingFirst().getUsername();
            if (username!=null) {
                messageService.sendMessage(new Message(username, sendText.getValue()));
                sendText.setValue("");
            }
        }));
        registrations.add(tabs.addSelectedChangeListener(selectedChangeEvent -> {
            tabsContent.removeAll();
            if (tabs.getSelectedTab() == chatTab)
                tabsContent.add(messages);
            else
                tabsContent.add(usersList);
        }));
    }

}
