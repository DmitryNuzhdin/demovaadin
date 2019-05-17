package com.example.demovaadin.ui;


import com.example.demovaadin.ui.auth.AuthWidget;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.currentTasks.CurrentTasksUI;
import com.example.demovaadin.ui.runNewTask.RunNewTaskUI;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;


@SpringComponent
@Route("")
@UIScope
@Push(transport = Transport.LONG_POLLING)
@StyleSheet("main.css")
public class MainUI extends HorizontalLayout implements UIWidget {
    //injects
    private CurrentTasksUI currentTasksUI;
    private RunNewTaskUI runNewTaskUI;
    private AuthWidget authWidgetUI;
    //UI components
    private Div leftSideLayout = new Div();
    private Div mainContentLayout = new Div();
    private Tabs tabs = new Tabs();
    private Tab currentTasksTab = new Tab("Current tasks");
    private Tab runNewTaskTab = new Tab("Run new task");
    private Tab logsTab = new Tab("Logs");
    
    public MainUI(CurrentTasksUI currentTasksUI, RunNewTaskUI runNewTaskUI, AuthWidget authWidget) {
        this.currentTasksUI = currentTasksUI;
        this.runNewTaskUI = runNewTaskUI;
        this.authWidgetUI = authWidget;
    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        autoSetCssClassNames();
        layout();
        createLogic();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {

    }

    private void createLogic() {
        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            currentTasksUI.setVisible(false);
            runNewTaskUI.setVisible(false);
            if(tabs.getSelectedTab()==currentTasksTab){
                currentTasksUI.setVisible(true);
            } else if (tabs.getSelectedTab()== runNewTaskTab){
                runNewTaskUI.setVisible(true);
            }
        });
    }

    private void layout(){
        add(leftSideLayout, mainContentLayout);
        leftSideLayout.add(authWidgetUI);
        mainContentLayout.add(tabs, currentTasksUI,runNewTaskUI);
        tabs.add(currentTasksTab, runNewTaskTab,logsTab);
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.setSelectedTab(currentTasksTab);
        currentTasksUI.setVisible(true);
        runNewTaskUI.setVisible(false);
    }


}
