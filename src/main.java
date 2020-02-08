import java.sql.*;
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

    public void BFS(Vertex source){
        visited.replace(source, true);
        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(source);
        while(!q.isEmpty()){
            source = q.poll();
            for(Vertex v: adjVertices.get(source)){
                if(!visited.get(v)){
                    q.add(v);
                }
            }
        }
    }
}

public class main {

    public static void main(String[] args) {
        try {
            Class.forName("org.sqlite.JDBC");
            //======archiva=====//
            String dbURL = "jdbc:sqlite:archiva.sqlite3";
            Connection conn = DriverManager.getConnection(dbURL);
            Statement stm = conn.createStatement();
            Graph graph = new Graph();
            //=====issue====//
            ResultSet rs = stm.executeQuery("SELECT * FROM issue");
            while (rs.next()) {
                String issue_id, assignee_username;
                issue_id = rs.getString("issue_id");
                assignee_username = rs.getString("assignee_username");
                if (assignee_username != null) {
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"), graph.addVertex(assignee_username, "Developer"));
                }
            }
            //=====issue====//
            //=====issue_component=====//
            rs = stm.executeQuery("SELECT * FROM issue_component");
            while (rs.next()) {
                String issue_id, component;
                issue_id = rs.getString("issue_id");
                component = rs.getString("component");
                graph.addEdge(graph.addVertex(issue_id, "issue_id"), graph.addVertex(component, "Component"));
            }
            //=====issue_component=====//
            //=====issue_comment=====//
            rs = stm.executeQuery("SELECT * FROM issue_comment");
            while (rs.next()) {
                String issue_id, comment;
                issue_id = rs.getString("issue_id");
                comment = rs.getString("username");
                graph.addEdge(graph.addVertex(issue_id, "issue_id"), graph.addVertex(comment, "comment"));
            }
            //=====issue_comment=====//
            graph.printGraph();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}