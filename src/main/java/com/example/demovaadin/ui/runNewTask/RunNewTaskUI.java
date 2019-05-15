package com.example.demovaadin.ui.runNewTask;

import com.example.demovaadin.service.run.ScriptRunService;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;


@SpringComponent
@UIScope
public class RunNewTaskUI extends VerticalLayout implements UIWidget {
    private ScriptRunService scriptRunService;
    private AuthenticationHolder authenticationHolder;
    private UniversalDisposable disposable = new UniversalDisposable();
    
    private Button submitButton = new Button("submit");
    private TextArea scriptTextArea = new TextArea();
    
    public RunNewTaskUI(ScriptRunService scriptRunService, AuthenticationHolder authenticationHolder) {
        this.scriptRunService = scriptRunService;
        this.authenticationHolder = authenticationHolder;
        autoSetCssClassNames();
        layout();
    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        disposable.add(submitButton.addClickListener(buttonClickEvent -> {
            scriptRunService.runScript(authenticationHolder
                .getAuthentication()
                .take(1)
                .blockingFirst()
                .getUsername(), scriptTextArea.getValue()
            );
            scriptTextArea.setValue("");
        }));
        super.onAttach(attachEvent);
    }
    
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposable.dispose();
        super.onDetach(detachEvent);
    }
    
    private void layout() {
        add(submitButton,scriptTextArea);
    }
}
