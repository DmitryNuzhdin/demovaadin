package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.service.run.ScriptRunService;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;




@SpringComponent
@UIScope
public class CurrentTasksUI extends Div implements UIWidget {
    private AuthenticationHolder authenticationHolder;
    private ScriptRunService scriptRunService;

    private Div tasksListLayout = new Div();
    private TaskDetails taskDetailsLayout = new TaskDetails();

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
        add(tasksListLayout, taskDetailsLayout);
    }

    private void createLogic() {
        tasksListLayout.removeAll();
        disposable.add(scriptRunService.getTasks()
            .subscribe(task -> access(()-> tasksListLayout.add(new TaskElement(task, taskDetailsLayout)))));
    }
}
