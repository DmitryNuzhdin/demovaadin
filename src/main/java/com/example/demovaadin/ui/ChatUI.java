package com.example.demovaadin.ui;

import com.example.demovaadin.backend.Authorization;
import com.example.demovaadin.backend.LoginService;
import com.example.demovaadin.backend.Message;
import com.example.demovaadin.backend.MessageService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Objects;


@SpringComponent
@UIScope
@Route("")
@Push(transport = Transport.LONG_POLLING)
public class ChatUI extends VerticalLayout  {
    private static final String COOKIE_NAME ="chat_auth_token";

    private VerticalLayout messages = new VerticalLayout();
    private VerticalLayout usersList = new VerticalLayout();

    private Tab chatTab = new Tab("Chat");
    private Tab usersTab = new Tab("Users");
    private Tabs tabs = new Tabs(chatTab, usersTab);
    private VerticalLayout tabsContent = new VerticalLayout();


    //private HorizontalLayout messagesAndUsersLayout = new HorizontalLayout(usersList,messages);

    private Button loginButton = new Button("Login", VaadinIcon.KEY.create());
    private Button registerButton = new Button("Register", VaadinIcon.PLUS.create());
    private TextField loginText = new TextField("login");
    private TextField passwordText = new TextField("password");
    private HorizontalLayout loginLayout = new HorizontalLayout(loginText, passwordText, loginButton, registerButton);

    private Label myLogin = new Label("");
    private TextField sendText = new TextField();
    private Button sendButton = new Button(VaadinIcon.ARROW_RIGHT.create());
    private HorizontalLayout sendLayout = new HorizontalLayout(myLogin, sendText,sendButton);

    private CompositeDisposable disposables = new CompositeDisposable();

    private LoginService loginService;
    private MessageService messageService;

    private Authorization authorization;

    public ChatUI(LoginService loginService, MessageService messageService) {
        this.loginService = loginService;
        this.messageService = messageService;
    }

    private void configureLayout(){
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
        authorize();
        createLogic();
        configureLayout();
        super.onAttach(attachEvent);
    }

    private void authorize() {
        Long token = null;

        for (Cookie cookie:VaadinService.getCurrentRequest().getCookies()) {
            if (Objects.equals(cookie.getName(), COOKIE_NAME)){
                token=Long.decode(cookie.getValue());
            }
        }
        authorization = loginService.connect(token);
        VaadinService.getCurrentResponse().addCookie(new Cookie(COOKIE_NAME, String.valueOf(authorization.getToken())));
    }

    private void createLogic() {
        disposables.addAll(
            authorization.errorMessage().subscribe(access(onErrorMessage())),
            authorization.user().subscribe(access(onUserName())),
            loginService.usersList().subscribe(access(onUserList())),
            messageService.messages().subscribe(access(onMessages()))
        );
        loginButton.addClickListener(loginButtonOnClick());
        registerButton.addClickListener(registerButtonOnclick());
        sendButton.addClickListener(sendButtonOnClick());
        this.addDetachListener(detachEvent -> {
            disposables.dispose();
            loginService.disconnect(authorization);
        });
        tabs.addSelectedChangeListener(event -> {
                if (event.getSource().getSelectedTab() == chatTab) {
                    tabsContent.removeAll();
                    tabsContent.add(messages);
                } else if (event.getSource().getSelectedTab() == usersTab) {
                    tabsContent.removeAll();
                    tabsContent.add(usersList);
                }
            }
        );


    }

    private ComponentEventListener<ClickEvent<Button>> sendButtonOnClick() {
        return buttonClickEvent -> {
            messageService.sendMessage(new Message(authorization.user().first("[null]").blockingGet(), sendText.getValue()));
            sendText.setValue("");
        };
    }

    private ComponentEventListener<ClickEvent<Button>> registerButtonOnclick() {
        return buttonClickEvent -> {
            loginService.register(authorization, loginText.getValue(), passwordText.getValue());
        };
    }


    private ComponentEventListener<ClickEvent<Button>> loginButtonOnClick() {
        return buttonClickEvent -> {
            loginService.login(authorization, loginText.getValue(), passwordText.getValue());
        };
    }

    private Consumer<List<Message>> onMessages() {
        return list -> {
            messages.removeAll();
            list.forEach(m->messages.add(new Label(m.getUser()+": "+m.getMessage())));
        };
    }

    private Consumer<String> onErrorMessage() {
        return string -> {
            new Notification(string,3000, Notification.Position.TOP_CENTER).open();
        };
    }

    private Consumer<String> onUserName() {
        return string -> {
            if (!string.equals("")) {
                myLogin.setText(string);
                add(sendLayout);
                remove(loginLayout);
            } else {
                remove(sendLayout);
                add(loginLayout);
            }
        };
    }

    private Consumer<List<String>> onUserList() {
        return strings -> {
            usersList.removeAll();
            strings.forEach(s -> usersList.add(new Label(s)));
        };
    }


    private <T> Consumer<T> access(Consumer<T> consumer){
        return t -> getUI().ifPresent(ui -> ui.access(() -> {
            try {
                consumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }));
    }
}
