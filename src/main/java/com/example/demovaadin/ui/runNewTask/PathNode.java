package com.example.demovaadin.ui.runNewTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PathNode {
    private String name;
    private Collection<PathNode> child = new ArrayList<>();
    private PathNode parent;
    private boolean isFile;
    
    public PathNode( PathNode parent,String name, boolean isFile) {
        this.name = name;
        this.parent = parent;
        this.isFile = isFile;
    }
    
    public String getName() {
        return name;
    }
    
    public Collection<PathNode> getChild() {
        return child;
    }
    
    public PathNode getParent() {
        return parent;
    }
    
    public boolean isFile() {
        return isFile;
    }
    
    public PathNode appendNode(String name, boolean isFile){
        if (this.isFile) throw new RuntimeException("cant append node to file");
        PathNode ans = new PathNode(this, name, isFile);
        child.add(ans);
        return ans;
    }
    
    public String getPath(){
        return getPath("");
    }
    
    private String getPath(String partOfPath){
        String myName = name + (isFile ? "" : "/");
        if (parent!=null) return parent.getPath(myName+partOfPath);
        return myName+partOfPath;
    }
    
    public static Collection<PathNode> example(){
        HashSet<PathNode> ans = new HashSet<>();
        PathNode pathNode = new PathNode(null,"folder1",false);
        pathNode.appendNode("folder2",false);
        pathNode.appendNode("folder3",false);
        pathNode.appendNode("file.txt",true);
        ans.add(pathNode);
        pathNode=new PathNode(null,"empty folder", false);
        ans.add(pathNode);
        return ans;
    }
}
