package com.example.demovaadin;


import com.example.demovaadin.ui.auth.AuthWidget;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@SpringComponent
@Route("")
@UIScope
@Push(transport = Transport.LONG_POLLING)
public class MainUI extends VerticalLayout {
    private HorizontalLayout header = new HorizontalLayout();
    private AuthWidget authWidget;

    public MainUI(AuthWidget authWidget) {
        this.authWidget = authWidget;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        add(header);
        header.add(new Label("WELCOME TO MY WEBSITE"));
        header.add(authWidget);
        super.onAttach(attachEvent);
    }
}
