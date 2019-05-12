package com.example.demovaadin;


import com.example.demovaadin.ui.auth.AuthWidget;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.currentTasks.CurrentTasksUI;
import com.vaadin.flow.component.AttachEvent;
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


@SpringComponent
@Route("")
@UIScope
@Push(transport = Transport.LONG_POLLING)
@StyleSheet("main.css")
public class MainUI extends HorizontalLayout implements UIWidget {
    //injects
    private CurrentTasksUI currentTasksUI;
    private AuthWidget authWidget;
    //UI components
    private VerticalLayout loginStatusPanel = new VerticalLayout();
    private VerticalLayout mainContent = new VerticalLayout();
    private Tabs mainContentTabs= new Tabs();
    private Tab currentTasksTab = new Tab("Current tasks");
    private Tab runNetTaskTab = new Tab("Current tasks");
    private Tab logsTab = new Tab("Logs");
    private VerticalLayout tabContent = new VerticalLayout();

    public MainUI(AuthWidget authWidget, CurrentTasksUI currentTasksUI) {
        this.authWidget = authWidget;
        this.currentTasksUI = currentTasksUI;
        layout();
        autoSetCssClassNames();
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        createLogic();
        super.onAttach(attachEvent);
    }

    private void createLogic() {
        mainContentTabs.addSelectedChangeListener(selectedChangeEvent -> {
            tabContent.removeAll();
            if(mainContentTabs.getSelectedTab()==currentTasksTab){
                tabContent.add(currentTasksUI);
            }
        });
        mainContentTabs.setSelectedTab(currentTasksTab);
        tabContent.removeAll();
        tabContent.add(currentTasksUI);
    }

    private void layout(){
        add(loginStatusPanel,mainContent);
        loginStatusPanel.add(authWidget);
        mainContent.add(mainContentTabs,tabContent);
        mainContentTabs.add(currentTasksTab,runNetTaskTab,logsTab);
    }


}
