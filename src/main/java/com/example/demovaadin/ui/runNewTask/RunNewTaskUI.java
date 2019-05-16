package com.example.demovaadin.ui.runNewTask;

import com.example.demovaadin.service.run.ScriptRunService;
import com.example.demovaadin.ui.auth.AuthenticationHolder;
import com.example.demovaadin.ui.common.UIWidget;
import com.example.demovaadin.ui.common.UniversalDisposable;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.ArrayList;
import java.util.Set;


@SpringComponent
@UIScope
public class RunNewTaskUI extends HorizontalLayout implements UIWidget {
    private ScriptRunService scriptRunService;
    private AuthenticationHolder authenticationHolder;
    private UniversalDisposable disposable = new UniversalDisposable();
    
    private VerticalLayout sourcePickLayout = new VerticalLayout();
    private VerticalLayout settingsLayout = new VerticalLayout();
    private Tabs sourceTabs = new Tabs();
    private Tab repoSourceTab = new Tab("From repository");
    private Tab runOnceSourceTab = new Tab("Run once");
    
    private TreeGrid<String> repoSourceTreeGrid = new TreeGrid<>();
    
    
    private Button refreshRepoButton = new Button("refresh repo");
    private Button submitButton = new Button("submit");
    private TextArea runOnceSourceTextArea = new TextArea();
    
    public RunNewTaskUI(ScriptRunService scriptRunService, AuthenticationHolder authenticationHolder) {
        this.scriptRunService = scriptRunService;
        this.authenticationHolder = authenticationHolder;
        autoSetCssClassNames();
        layout();
    }
    
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        createLogic();
    }
    
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        disposable.dispose();
        super.onDetach(detachEvent);
    }
    
    private void createLogic(){
        disposable.add(refreshRepoButton.addClickListener(buttonClickEvent -> {
            NexusConnector nexusConnector = new NexusConnector();
            
            repoSourceTreeGrid.setItems(nexusConnector.getAssets());
            repoSourceTreeGrid.getDataProvider().refreshAll();
        }));
        
        disposable.add(sourceTabs.addSelectedChangeListener(selectedChangeEvent -> {
            repoSourceTreeGrid.setVisible(false);
            runOnceSourceTextArea.setVisible(false);
            if(sourceTabs.getSelectedTab()==repoSourceTab){
                repoSourceTreeGrid.setVisible(true);
            } else if(sourceTabs.getSelectedTab()==runOnceSourceTab) {
                runOnceSourceTextArea.setVisible(true);
            }
        }));
        disposable.add(submitButton.addClickListener(buttonClickEvent -> {
            String script;
            Set<String> set= repoSourceTreeGrid.getSelectedItems();
            if (set.size()==1) {
                scriptRunService.runScript(authenticationHolder
                    .getAuthentication()
                    .take(1)
                    .blockingFirst()
                    .getUsername(), (new NexusConnector()).getFile(set.iterator().next())
                );
            }
            runOnceSourceTextArea.setValue("");
        }));
    }
    
    private void layout() {
        add(sourcePickLayout,settingsLayout);
        sourcePickLayout.add(sourceTabs, repoSourceTreeGrid, runOnceSourceTextArea,refreshRepoButton, submitButton);
        sourceTabs.add(repoSourceTab,runOnceSourceTab);
        sourceTabs.setSelectedTab(repoSourceTab);
        repoSourceTreeGrid.setVisible(true);
        runOnceSourceTextArea.setVisible(false);
        
        //repoSourceTreeGrid.setItems(PathNode.example(), PathNode::getChild);
        repoSourceTreeGrid.addHierarchyColumn(String::toString);
    }
}
