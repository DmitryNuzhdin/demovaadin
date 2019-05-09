package com.example.demovaadin.ui.auth;

import com.example.demovaadin.authService.Authentication;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


@Component
@UIScope
public class AuthWidget extends HorizontalLayout {
    //Injects
    private AuthenticationHolder authenticationHolder;
    //Disposable for unsubscribe
    private CompositeDisposable disposables = new CompositeDisposable();
    private Set<Registration> registrations = new HashSet<>();
    //UI components
    private Label loggedInAs = new Label("You are logged in as:");
    private Label currentUsername = new Label(null);
    private TextField username = new TextField("username");
    private TextField password = new TextField("password");
    private Button logIn = new Button("logIn", VaadinIcon.KEY.create());
    private Button logOut = new Button("logOut", VaadinIcon.ARROW_RIGHT.create());
    //UI commands
    private final Consumer<String> SHOW_NOTIFICATION = (s) -> access(()->new Notification(s,3000, Notification.Position.TOP_CENTER).open());
    private final Consumer<Authentication> SWITCH_STATUS_OR_FIELDS = (a) -> access(() -> {
        if (a.isAuthorized()) {
            removeAll();
            add(loggedInAs, currentUsername, logOut);
            currentUsername.setText(a.getUsername());
        } else {
            removeAll();
            add(username,password, logIn);
        }
    });


    public AuthWidget(AuthenticationHolder authenticationHolder) {
        this.authenticationHolder = authenticationHolder;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        createLogic();
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposables.dispose();
        registrations.forEach(Registration::remove);
        super.onDetach(detachEvent);
    }

    private void createLogic() {
        registrations.add(logIn.addClickListener(e ->  {
            authenticationHolder
                    .authenticate(username.getValue(), password.getValue())
                    .subscribe(SHOW_NOTIFICATION);
            username.setValue("");
            password.setValue("");
            })
        );
        registrations.add(logOut.addClickListener(e ->  authenticationHolder.logOut()));
        disposables.add(authenticationHolder
            .getAuthentication()
            .subscribe(SWITCH_STATUS_OR_FIELDS));
    }

    private void access(Command command){
        getUI().ifPresent(ui -> ui.access(command));
    }
}
