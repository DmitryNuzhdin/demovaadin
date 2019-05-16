package com.example.demovaadin;


import com.example.demovaadin.service.run.ReactiveStringWriter;
import com.example.demovaadin.ui.runNewTask.NexusConnector;
import io.reactivex.Observable;
import net.bytebuddy.dynamic.Nexus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JustTest {
    public static void main(String[] args){
        //System.out.println((new NexusConnector()).getFile("repository/scripts_repo/scripts/t1"));
        //System.out.println((new NexusConnector()).getFile("service/rest/v1/assets?repository=scripts_repo"));
        System.out.println(new NexusConnector().getAssets());
    }
}
