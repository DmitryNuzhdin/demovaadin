package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.service.run.Task;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;


public class TaskElement extends Div implements UIWidget {
    private Task task;
    private TaskDetails taskDetailsLayout;

    private Label username = new Label();
    private Label script = new Label();
    private Button b = new Button();

    private UniversalDisposable disposable = new UniversalDisposable();


    public TaskElement(Task task, TaskDetails taskDetails) {
        this.task=task;
        this.taskDetailsLayout =taskDetails;
        username.setText(task.getUser());
        script.setText(task.getScript());
        add(username,script);
        autoSetCssClassNames();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        disposable.add(task.getStatus().subscribe(taskStatus -> access(()-> {
            getElement().setAttribute("status", taskStatus.name().toLowerCase());
        }
        )));
        disposable.add(getElement().addEventListener("click",domEvent -> taskDetailsLayout.setTask(task)));
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposable.dispose();
        super.onDetach(detachEvent);
    }

    public Task getTask() {
        return task;
    }
}
