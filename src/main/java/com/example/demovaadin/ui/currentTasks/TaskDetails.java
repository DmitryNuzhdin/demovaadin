package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.service.run.Task;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class TaskDetails extends VerticalLayout implements UIWidget{

    private Label captionUsernameLabel = new Label("username");
    private Label captionStatusLabel = new Label("status");
    private Label usernameLabel = new Label();
    private Label statusLabel = new Label();
    private HorizontalLayout usernameLayout = new HorizontalLayout();
    private HorizontalLayout statusLayout = new HorizontalLayout();
    private TextArea resultTextAria = new TextArea();

    private Task task;
    private UniversalDisposable disposable = new UniversalDisposable();

    public TaskDetails() {
        layout();
    }

    public void setTask(Task task) {
        this.task = task;
        onAttach(null);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        disposable.dispose();
        createLogic();
        super.onAttach(attachEvent);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposable.dispose();
        super.onDetach(detachEvent);
    }

    private void layout() {
        add(usernameLayout,statusLayout,resultTextAria);
        usernameLayout.add(captionUsernameLabel,usernameLabel);
        statusLayout.add(captionStatusLabel,statusLabel);
        resultTextAria.setEnabled(false);
    }

    private void createLogic() {
        usernameLabel.setText("");
        statusLabel.setText("");
        resultTextAria.setValue("");
        if (task!=null){
            usernameLabel.setText(task.getUser());
            disposable.add( task.getStatus().subscribe(taskStatus -> access(()-> statusLabel.setText(taskStatus.name()))));
            disposable.add( task.getResult().subscribe(o -> access(()->{
                statusLabel.setText(statusLabel.getText()+"\n"+o.toString());
                })
            ));
        }
    }
}
