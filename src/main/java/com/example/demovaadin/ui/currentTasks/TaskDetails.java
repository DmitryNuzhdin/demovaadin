package com.example.demovaadin.ui.currentTasks;

import com.example.demovaadin.service.run.Task;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;

public class TaskDetails extends Div implements UIWidget{

    private Label captionUsernameLabel = new Label("username");
    private Label captionStatusLabel = new Label("status");
    private Label usernameLabel = new Label();
    private Label statusLabel = new Label();
    private Div usernameRow = new Div();
    private Div statusRow = new Div();
    private Paragraph resultScrollableFullsizeParagraph = new Paragraph();

    private Task task;
    private UniversalDisposable disposable = new UniversalDisposable();

    public TaskDetails() {
        layout();
        autoSetCssClassNames();
    }

    public void setTask(Task task) {
        this.task = task;
        createLogic();

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
        add(usernameRow, statusRow, resultScrollableFullsizeParagraph);
        usernameRow.add(captionUsernameLabel,usernameLabel);
        statusRow.add(captionStatusLabel,statusLabel);
    }

    private void createLogic() {
        disposable.dispose();
        usernameLabel.setText("");
        statusLabel.setText("");
        resultScrollableFullsizeParagraph.setText("");
        if (task!=null){
            usernameLabel.setText(task.getUser());
            disposable.add( task.getStatus().subscribe(taskStatus -> access(()-> statusLabel.setText(taskStatus.name()))));
            disposable.add( task.getResult().subscribe(o -> access(()->{
                resultScrollableFullsizeParagraph.setText(resultScrollableFullsizeParagraph.getText()+o.toString()+"\n");
                })
            ));
        }
    }
}
