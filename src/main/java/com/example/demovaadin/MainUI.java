package com.example.demovaadin;


import com.example.demovaadin.ui.auth.AuthWidget;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.currentTasks.CurrentTasksUI;
import com.example.demovaadin.ui.runNewTask.RunNewTaskUI;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.ui.Transport;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import io.reactivex.disposables.CompositeDisposable;


@SpringComponent
@Route("")
@UIScope
@Push(transport = Transport.LONG_POLLING)
@StyleSheet("main.css")
public class MainUI extends HorizontalLayout implements UIWidget {
    //injects
    private CurrentTasksUI currentTasksUI;
    private RunNewTaskUI runNewTaskUI;
    private AuthWidget authWidget;
    //UI components
    private VerticalLayout loginStatusPanel = new VerticalLayout();
    private VerticalLayout mainContent = new VerticalLayout();
    private Tabs mainContentTabs= new Tabs();
    private Tab currentTasksTab = new Tab("Current tasks");
    private Tab runNewTaskTab = new Tab("Run new task");
    private Tab logsTab = new Tab("Logs");
    private VerticalLayout tabContent = new VerticalLayout();
    
    public MainUI(CurrentTasksUI currentTasksUI, RunNewTaskUI runNewTaskUI, AuthWidget authWidget) {
        this.currentTasksUI = currentTasksUI;
        this.runNewTaskUI = runNewTaskUI;
        this.authWidget = authWidget;
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
        mainContentTabs.addSelectedChangeListener(selectedChangeEvent -> {
            currentTasksUI.setVisible(false);
            runNewTaskUI.setVisible(false);
            if(mainContentTabs.getSelectedTab()==currentTasksTab){
                currentTasksUI.setVisible(true);
            } else if (mainContentTabs.getSelectedTab()== runNewTaskTab){
                runNewTaskUI.setVisible(true);
            }
        });
    }

    private void layout(){
        add(loginStatusPanel,mainContent);
        loginStatusPanel.add(authWidget);
        mainContent.add(mainContentTabs,tabContent);
        mainContentTabs.add(currentTasksTab, runNewTaskTab,logsTab);
        mainContentTabs.setFlexGrowForEnclosedTabs(1);
        tabContent.add(currentTasksUI,runNewTaskUI);
        mainContentTabs.setSelectedTab(currentTasksTab);
        currentTasksUI.setVisible(true);
        runNewTaskUI.setVisible(false);
    }


}
