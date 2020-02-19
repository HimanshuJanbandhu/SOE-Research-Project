package net.codejava.jdbc;

import java.sql.*;
import java.lang.*;
import java.util.*;

class Vertex{
    String Name;
    String type;
    Vertex(String Name,String type){
        this.Name=Name;
        this.type=type;
    }
    @Override
    public boolean equals(Object object)
    {
        Vertex vertex = (Vertex) object;
        return ((vertex.Name.compareTo(Name)==0) && (vertex.type.compareTo(type)==0));
    }
}

class Graph {

    public Map<Vertex, ArrayList<Vertex>> adjVertices = new Hashtable<Vertex,ArrayList<Vertex>>();

    public Map<Vertex,Boolean> visited = new Hashtable<Vertex,Boolean>();

    String AICIA[] = {"issue_id","Component","issue_id","assignee"};

    Vertex currentSource;

    public Vertex addVertex(String Name,String type) {
        for(Vertex v : adjVertices.keySet()){
            if(v.Name.compareTo(Name)==0 && v.type.compareTo(type)==0){
                return v;
            }
        }
        Vertex v = new Vertex(Name,type);
        ArrayList<Vertex> List = new ArrayList<Vertex>();
        adjVertices.put(v, List);
        visited.put(v,false);
        return v;
    }

    public void addEdge(Vertex v,Vertex u) {
        adjVertices.get(v).add(u);
        adjVertices.get(u).add(v);
    }

    public void printGraph() {
        for(Vertex i : adjVertices.keySet()){
            System.out.print(i.Name + "--> ");
            for (Vertex j : adjVertices.get(i)) {
                System.out.print(j.Name + " ");
            }
            System.out.println("");
        }
    }
    public int aicia=0;
    public void DFS_MetaPaths(Vertex source,int step){
        //System.out.print(source.Name + " ");
        visited.replace(source,true);
        if ((source.type.compareTo(AICIA[AICIA.length-1]) == 0)  && !(currentSource.Name.compareTo(source.Name)==0)) {
            //System.out.println(source.Name);
            aicia++;
        }
        for(Vertex v: adjVertices.get(source)){
            if(step<AICIA.length) {
                 if ((v.type.compareTo(AICIA[step]) == 0) && !visited.get(v)) {
                    DFS_MetaPaths(v, 1+step);
                }
            }
        }
        visited.replace(source,false);
    }
    int pathCount=0;
    Vertex A,B;
    public void CountPaths(Vertex u,Vertex v,int step){
        //visited.replace(u,true);
        if(u==v){
            pathCount++;
        }
        else{
            for(Vertex V: adjVertices.get(u)){
                if(step<AICIA.length){
                    if (/*!visited.get(V) &&*/ (V.type.compareTo(AICIA[step])==0 )) {
                        CountPaths(V,v,1+step);
                    }
                }
            }
        }
        //visited.replace(u,false);
    }
}

public class JdbcSQLiteConnection {

    public static void main(String[] args) {
        try {
//            Class.forName("org.sqlite.JDBC");
//            //======archiva=====//
//            String dbURL = "jdbc:sqlite:DatabasesSOE/archiva.sqlite3";
//            Connection conn = DriverManager.getConnection(dbURL);
//            Statement stm = conn.createStatement();
//            Graph graph= new Graph();
//            //=====issue====//
//            ResultSet rs = stm.executeQuery("SELECT * FROM issue");
//            while(rs.next()){
//                String issue_id,assignee_username;
//                issue_id = rs.getString("issue_id");
//                assignee_username = rs.getString("assignee_username");
//                if(assignee_username!=null) {
//                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(assignee_username, "assignee"));
//                }
//            }
//            //=====issue====//
//            //=====issue_component=====//
//            rs = stm.executeQuery("SELECT * FROM issue_component");
//            while(rs.next()){
//                String issue_id,component;
//                issue_id = rs.getString("issue_id");
//                component = rs.getString("component");
//                graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(component, "Component"));
//            }
//            //=====issue_component=====//
//            //=====issue_comment=====//
//            rs = stm.executeQuery("SELECT * FROM issue_comment");
//            while(rs.next()){
//                String issue_id,comment;
//                issue_id = rs.getString("issue_id");
//                comment = rs.getString("username");
//                graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(comment, "comment"));
//            }
//            //=====issue_comment=====//
//            //graph.printGraph();
//            //count aicia
//            for(Vertex v : graph.adjVertices.keySet()){
//                if((!graph.visited.get(v)) && (v.type.compareTo("assignee")==0)){
//                    graph.DFS_AICIA(v,0);
//                }
//            }
//            System.out.println(graph.aicia);
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:DatabasesSOE/testdb";
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stm = conn.createStatement();
            Graph graph= new Graph();
            
//             ================================
            Map<String,String>issue_to_file= new HashMap<String,String>();
            Map<String,String>commithash_to_issue=new HashMap<String,String>();
            Map<String,String>username=new HashMap<String,String>();
//                                       =====issue=====//
//            System.out.println("Connected");

            ResultSet rs = stm.executeQuery("SELECT * FROM issue");

            while (rs.next())
            {
                String user=rs.getString("assignee");
                String u_name=rs.getString("assignee_username");
                username.put(user,u_name);
            }

             rs = stm.executeQuery("SELECT * FROM change_set_link");
            while(rs.next())
            {
                String issue_id=rs.getString("issue_id");
                String hash = rs.getString("commit_hash");
                commithash_to_issue.put(issue_id,hash);
            }

//            System.out.println(username.get("nicolas de loof"));

            rs=stm.executeQuery("SELECT * FROM code_change");
//          ResultSet rs = stm.executeQuery("SELECT * FROM sample2");
            while(rs.next())
            {
                String hash,file;
                hash=rs.getString("commit_hash");
                file=rs.getString("file_path");
                issue_to_file.put(commithash_to_issue.get(hash),file);
            }
            System.out.println("Done");
            
            
//             =======================================
            //=====issue====//
             rs = stm.executeQuery("SELECT * FROM issue");
            while(rs.next()){
                String issue_id,assignee_username;
                issue_id = rs.getString("issue_id");
                assignee_username = rs.getString("assignee_username");
                if(assignee_username!=null) {
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(assignee_username, "assignee"));
                }
            }
            rs = stm.executeQuery("SELECT * FROM issue_component");
            while(rs.next()){
                String issue_id,component;
                issue_id = rs.getString("issue_id");
                component = rs.getString("component");
                graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(component, "Component"));
            }
            //graph.printGraph();
            for(Vertex v : graph.adjVertices.keySet()){
                if( (v.type.compareTo("assignee")==0)){
                    graph.currentSource = v;
                    //System.out.print(v.Name);
                    graph.DFS_MetaPaths(v,0);
                }
                //System.out.println("");
                //System.out.println((graph.aicia));
            }
            System.out.println((graph.aicia/2));
            for(Vertex v : graph.adjVertices.keySet()){
                if(v.Name.compareTo("C")==0){
                    graph.A=v;
                }
                else if(v.Name.compareTo("D")==0){
                    graph.B=v;
                }
            }
            graph.CountPaths(graph.A,graph.B,0);
            System.out.println(graph.pathCount);

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
