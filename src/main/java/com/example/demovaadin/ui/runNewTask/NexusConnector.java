package com.example.demovaadin.ui.runNewTask;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

public class NexusConnector {
    private final static String NEXUS_PATH = "http://admin:admin321@51.158.106.195:8081/";
    private final static String REPO_PATH = "repository/scripts_repo/";
    
    
    public String getFile(String path){
        try {
            URL url = new URL(NEXUS_PATH +REPO_PATH+path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            StringBuilder stringBuilder = new StringBuilder();
            
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            in.close();
            connection.disconnect();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Collection<String> getAssets(){
        try {
            URL url = new URL(NEXUS_PATH +"service/rest/v1/assets?repository=scripts_repo");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
        
            StringBuilder stringBuilder = new StringBuilder();
        
            BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            in.close();
            connection.disconnect();
    
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(stringBuilder.toString());
    
            ArrayList<String> ans = new ArrayList<>();
            
            for (Object o :(JSONArray) jsonObject.get("items")){
                JSONObject asset = (JSONObject) o;
                ans.add((String) asset.get("path"));
            }
            return ans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
