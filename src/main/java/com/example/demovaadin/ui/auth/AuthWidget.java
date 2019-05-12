package com.example.demovaadin.ui.auth;

import com.example.demovaadin.service.auth.Authentication;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.functions.Consumer;


@SpringComponent
@UIScope
public class AuthWidget extends VerticalLayout implements UIWidget {
    //Injects
    private AuthenticationHolder authenticationHolder;
    //Disposable for unsubscribe
    private UniversalDisposable disposable = new UniversalDisposable();
    //UI components

    private Label currentUsername = new Label("");
    private TextField usernameField = new TextField("username:");
    private TextField passwordField = new TextField("password:");
    private Button logInButton = new Button("log in", VaadinIcon.KEY.create());
    private Button logOutButton = new Button("log out", VaadinIcon.ARROW_LEFT.create());
    //UI commands
    private final Consumer<Authentication> SWITCH_STATUS_OR_FIELDS = (a) -> access(() -> {
        if (a.getUsername()!=null) {
            removeAll();
            add(currentUsername, logOutButton);
            currentUsername.setText(a.getUsername());
        } else {
            removeAll();
            add(usernameField, passwordField, logInButton);
        }
    });


    public AuthWidget(AuthenticationHolder authenticationHolder) {
        this.authenticationHolder = authenticationHolder;
        layout();
        autoSetCssClassNames();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        createLogic();
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposable.dispose();
        super.onDetach(detachEvent);
    }

    private void layout(){
        add(currentUsername,usernameField,passwordField,logInButton,logOutButton);
    }

    private void createLogic() {
        disposable.add(logInButton.addClickListener(e ->  {
            authenticationHolder.authenticate(usernameField.getValue(), passwordField.getValue());
            usernameField.setValue("");
            passwordField.setValue("");
            })
        );

        disposable.add(logOutButton.addClickListener(e ->  authenticationHolder.logOut()));

        disposable.add(authenticationHolder
            .getAuthentication()
            .subscribe(authentication -> {
                if (authentication.getUsername()==null){
                    currentUsername.setText("");
                    usernameField.setVisible(true);
                    passwordField.setVisible(true);
                    logInButton.setVisible(true);
                    logOutButton.setVisible(false);
                } else {
                    currentUsername.setText(authentication.getUsername());
                    usernameField.setVisible(false);
                    passwordField.setVisible(false);
                    logInButton.setVisible(false);
                    logOutButton.setVisible(true);
                }
            }));
    }
}
