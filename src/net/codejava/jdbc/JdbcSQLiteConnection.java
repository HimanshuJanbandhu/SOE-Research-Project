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
            System.out.print(i.type + "--> ");
            for (Vertex j : adjVertices.get(i)) {
                System.out.print(j.type + " ");
            }
            System.out.println("");
        }
    }
    public int aicia=0;
    public void DFS_AICIA(Vertex source,int step){
//        visited.replace(source, true);
//        Queue<Vertex> q = new LinkedList<Vertex>();
//        q.add(source);
//        while(!q.isEmpty()){
//            source = q.poll();
//            for(Vertex v: adjVertices.get(source)){
//                if(!visited.get(v)){
//                    q.add(v);
//                }
//            }
//        }
        if(source.type.compareTo("issue_id")==0) {
            visited.replace(source, true);
        }
        System.out.print(source.Name+" ");
        if ((source.type.compareTo("assignee") == 0)  && !(currentSource.Name.compareTo(source.Name)==0)) {
            //System.out.println(source.Name);
            aicia++;
        }
        for(Vertex v: adjVertices.get(source)){
            if(step<4) {
                if ((v.type.compareTo(AICIA[step]) == 0) && v.type.compareTo("issue_id")==0) {
                    if(!visited.get(v)) {
                        DFS_AICIA(v, 1 + step);
                    }
                }
                else if ((v.type.compareTo(AICIA[step]) == 0)) {
                    DFS_AICIA(v, 1+step);
                }
            }
        }
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
            String dbURL = "jdbc:sqlite:DatabasesSOE/data.sqlite";
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stm = conn.createStatement();
            Graph graph= new Graph();
            //=====issue====//
            ResultSet rs = stm.executeQuery("SELECT * FROM sample2");
            while(rs.next()){
                String issue_id,assignee_username;
                issue_id = rs.getString("issue_id");
                assignee_username = rs.getString("assignee_username");
                if(assignee_username!=null) {
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(assignee_username, "assignee"));
                }
            }
            rs = stm.executeQuery("SELECT * FROM sample1");
            while(rs.next()){
                String issue_id,component;
                issue_id = rs.getString("issue_id");
                component = rs.getString("component");
                graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(component, "Component"));
            }
            //graph.printGraph();
            int devC=0;
            for(Vertex v : graph.adjVertices.keySet()){
                if( (v.type.compareTo("assignee")==0)){
                    graph.currentSource = v;
                    devC++;
                    //System.out.print(v.Name);
                    graph.DFS_AICIA(v,0);
                }
                System.out.println("");
                System.out.println((graph.aicia));
            }
            System.out.println((graph.aicia));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}