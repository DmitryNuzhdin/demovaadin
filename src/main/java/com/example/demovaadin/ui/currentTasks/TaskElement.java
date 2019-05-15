package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.service.run.Task;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@StyleSheet("taskelement.css")
public class TaskElement extends VerticalLayout implements UIWidget {
    private Task task;
    private TaskDetails taskDetails;

    private Label username = new Label();
    private Label script = new Label();
    private Button b = new Button();

    private UniversalDisposable disposable = new UniversalDisposable();


    public TaskElement(Task task, TaskDetails taskDetails) {
        this.task=task;
        this.taskDetails=taskDetails;
        username.setText(task.getUser());
        script.setText(task.getScript());
        add(username,script);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        disposable.add(task.getStatus().subscribe(taskStatus -> access(()-> {
            addClassName(taskStatus.name().toLowerCase());
        }
        )));
        disposable.add(getElement().addEventListener("click",domEvent -> taskDetails.setTask(task)));
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
