package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.service.auth.Authentication;
import com.example.demovaadin.service.auth.Role;
import com.example.demovaadin.service.run.ScriptRunService;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;




@SpringComponent
@UIScope
public class CurrentTasksUI extends HorizontalLayout implements UIWidget {
    private AuthenticationHolder authenticationHolder;
    private ScriptRunService scriptRunService;

    private VerticalLayout tasksList = new VerticalLayout();
    private TaskDetails taskDetails = new TaskDetails();

    private UniversalDisposable disposable = new UniversalDisposable();

    public CurrentTasksUI(AuthenticationHolder authenticationHolder, ScriptRunService scriptRunService) {
        this.authenticationHolder = authenticationHolder;
        this.scriptRunService = scriptRunService;
        autoSetCssClassNames();
        layout();
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

    private void layout() {
        add(tasksList, taskDetails);
    }

    private void createLogic() {
        tasksList.removeAll();
        disposable.add(scriptRunService.getTasks()
            .subscribe(task -> access(()-> tasksList.add(new TaskElement(task,taskDetails)))));
    }
}
