package com.example.demovaadin;


import com.example.demovaadin.service.run.ReactiveStringWriter;
import com.example.demovaadin.service.run.Task;
import com.example.demovaadin.ui.runNewTask.NexusConnector;
import io.reactivex.Observable;
import net.bytebuddy.dynamic.Nexus;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class JustTest {
    public static void main(String[] args){
        //System.out.println((new NexusConnector()).getFile("repository/scripts_repo/scripts/t1"));
        //System.out.println((new NexusConnector()).getFile("service/rest/v1/assets?repository=scripts_repo"));
        //System.out.println(new NexusConnector().getAssets());
        //
        //System.out.println(Arrays.asList("autoSetCssClassNames".split("(?<=[a-z,0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])")));
        Task t = new Task(null, "text1 /* txt2 \n kappa \n lol */ text2 /* lul */", true);
        t.initAttributes();
    }
}
