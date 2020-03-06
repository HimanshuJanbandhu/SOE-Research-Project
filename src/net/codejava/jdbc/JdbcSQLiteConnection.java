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

    public Map<Vertex, String> Summary = new Hashtable<Vertex, String>();

    public Map<Vertex, String> Description = new Hashtable<Vertex, String>();

    Vertex for_ngram;

    String AICIA[] = {"issue_id","Component","issue_id","assignee"};
    String DICIF[] ={"issue_id","Component","issue_id"};

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
    public void DFS_MetaPaths(Vertex source,int step, String path[]){
        //System.out.print(source.Name + " ");
        visited.replace(source,true);
        if ((source.type.compareTo(path[path.length-1]) == 0)  && !(currentSource.Name.compareTo(source.Name)==0)) {
            //System.out.println(source.Name);
            aicia++;
        }
        for(Vertex v: adjVertices.get(source)){
            if(step<AICIA.length) {
                 if ((v.type.compareTo(path[step]) == 0) && !visited.get(v)) {
                    DFS_MetaPaths(v, 1+step,path);
                }
            }
        }
        visited.replace(source,false);
    }

    public int getAicia(Vertex source,String path[]) {
        aicia=0;
        DFS_MetaPaths(source,1,path);
        return aicia;
    }

    int pathCount=0;
    Vertex A,B;
    public void CountPaths(Vertex u,Vertex v,int step, String path[]){
        visited.replace(u,true);
        if(u==v){
            pathCount++;
        }
        else{
            for(Vertex V: adjVertices.get(u)){
                if(step<path.length){
                    if (/*!visited.get(V) &&*/ (V.type.compareTo(path[step])==0 )) {
                        CountPaths(V,v,1+step,path);
                    }
                }
            }
        }
        visited.replace(u,false);
    }

    public int getPathCount(Vertex u,Vertex v,String path[]){
        pathCount=0;
        CountPaths(u,v,1,path);
        return pathCount;
    }

    public int NormalisedCountPaths(Vertex u,Vertex v, String path[]) {
        int PCR = getPathCount(u,v,path);
        String[] revPath = new String[path.length];
        int j = path.length;
        for (int i = 0; i < path.length; i++) {
            revPath[j - 1] = path[i];
            j = j - 1;
        }
        int PCinR = getPathCount(v,u,revPath);
        int PCRa_ = getAicia(u,path);
        int PCR_a = getAicia(v,revPath);

        return (PCR + PCinR)/(PCRa_ + PCR_a);
    }

    public int RandomWalk(Vertex u, Vertex v, String path[]) {

        int PCR = getPathCount(u,v,path);
        int PCRa_ = getAicia(u,path);

        return  (PCR/PCRa_);
    }

    public int SymmetricRandomWalk(Vertex u, Vertex v, String path[]) {
        String[] revPath = new String[path.length];
        int j = path.length;
        for (int i = 0; i < path.length; i++) {
            revPath[j - 1] = path[i];
            j = j - 1;
        }
        return RandomWalk(u,v,path) + RandomWalk(v,u,revPath);
    }
}

class MetaPaths {
    String AICF[] = {"assignee","issue_id","commit_hash","file"};
    String CICF[] = {"commenter","issue_id","commit_hash","file"};
    String CICICF[] = {"commenter","issue_id","commenter","issue_id","commit_hash","file"};
    String AICICF[] = {"assignee","issue_id","commenter","issue_id","commit_hash","file"};
    String CIAICF[] = {"commenter","issue_id","assignee","issue_id","commit_hash","file"};
    String AICFCICICF[] = {"assignee","issue_id","commit_hash","file","commit_hash","issue_id","component","issue_id","commit_hash","file"};
    String _AICICF[] = {"assignee","issue_id","component","issue_id","commit_hash","file"};
    String _CICICF[] = {"commenter","issue_id","component","issue_id","commit_hash","file"};
    String AIRICF[] = {"assignee","issue_id","reporter","issue_id","commit_hash","file"};
    String CIRICF[] = {"commenter","issue_id","reporter","issue_id","commit_hash","file"};
    String __AICICF[] = {"assignee","issue_id","component","issue_id","commit_hash","file"};
    String __CICICF[] = {"commenter","issue_id","component","issue_id","commit_hash","file"};
    String AIAICF[] = {"assignee","issue_id","assignee","assignee","issue_id","commit_hash","file"};
    String _CIAICF[] = {"commenter","issue_id","assignee","assignee","issue_id","commit_hash","file"};
    String AICFCICF[] = {"assignee","issue_id","commit_hash","file","commit_hash","issue_id","commit_hash","file"};
}

class Ngrams {

    public static <String> Set<String> mergeSet(Set<String> a, Set<String> b)
    {

        // Creating an empty set
        Set<String> mergedSet = new HashSet<String>();

        // add the two sets to be merged
        // into the new set
        mergedSet.addAll(a);
        mergedSet.addAll(b);

        // returning the merged set
        return mergedSet;
    }

    public Vertex A,B;

    public Map<String, Integer> Ngrams(int n, String str) {
        Map<String, Integer> ngrams = new Hashtable<String, Integer>();
        for (int i = 0; i < str.length() - n + 1; i++) {
            String s = str.substring(i, i + n);
            if(ngrams.containsKey(s)){
                ngrams.put(s,ngrams.get(s)+1);
            }
            else{
                ngrams.put(s,1);
            }
        }
        return ngrams;
    }

    public void Print_Ngrams(Map<String,Integer> ngrams1, Map<String,Integer> ngrams2){
        Set<String> Set_Ngrams = mergeSet(ngrams1.keySet(),ngrams2.keySet());
        int no_of_Ngrams1;
        int no_of_Ngrams2;
        for(String s : Set_Ngrams){
            if(ngrams1.containsKey(s)){
                no_of_Ngrams1=ngrams1.get(s);
            }
            else{
                no_of_Ngrams1=0;
            }
            if(ngrams2.containsKey(s)){
                no_of_Ngrams2=ngrams2.get(s);
            }
            else{
                no_of_Ngrams2=0;
            }
            System.out.printf("%s   %d   %d\n",s,no_of_Ngrams1,no_of_Ngrams2);
        }
    }
}
public class JdbcSQLiteConnection {

    public static void main(String[] args) {
        try {
                Class.forName("org.sqlite.JDBC");
                //======archiva=====//
                String dbURL = "jdbc:sqlite:DatabasesSOE/archiva.sqlite3";
                Connection conn = DriverManager.getConnection(dbURL);
                Statement stm = conn.createStatement();
                Graph graph= new Graph();
                //=====issue====//
                ResultSet rs = stm.executeQuery("SELECT * FROM issue");
                while(rs.next()){
                    String issue_id,assignee_username,reporter_username;
                    issue_id = rs.getString("issue_id");
                    assignee_username = rs.getString("assignee_username");
                    reporter_username = rs.getString("reporter_username");
                    if(assignee_username!=null) {
                        graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(assignee_username, "assignee"));
                    }
                    if(reporter_username!=null) {
                        graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(reporter_username, "reporter"));
                    }
                }
                //=====issue====//
                //=====issue_component=====//
                rs = stm.executeQuery("SELECT * FROM issue_component");
                while(rs.next()){
                    String issue_id,component;
                    issue_id = rs.getString("issue_id");
                    component = rs.getString("component");
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(component, "component"));
                }
                //=====issue_component=====//
                //=====issue_comment=====//
                rs = stm.executeQuery("SELECT * FROM issue_comment");
                while(rs.next()){
                    String issue_id,comment;
                    issue_id = rs.getString("issue_id");
                    comment = rs.getString("username");
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(comment, "commenter"));
                }
                //=====issue_comment=====//
                //=====change_set_link=====//
                rs = stm.executeQuery("SELECT * FROM change_set_link");
                while(rs.next()){
                    String issue_id,commit_hash;
                    issue_id = rs.getString("issue_id");
                    commit_hash = rs.getString("commit_hash");
                    graph.addEdge(graph.addVertex(issue_id, "issue_id"),graph.addVertex(commit_hash, "commit_hash"));
                }
                //=====change_set_link=====//
                //=====code_change=====//
                rs = stm.executeQuery("SELECT * FROM code_change");
                while(rs.next()){
                    String file_path,commit_hash;
                    file_path = rs.getString("file_path");
                    commit_hash = rs.getString("commit_hash");
                    graph.addEdge(graph.addVertex(file_path, "file"),graph.addVertex(commit_hash, "commit_hash"));
                }
                //=====code_change=====//
                //PathCount;
                //NormalisedPathCount;
                //======N grams=====//
//                rs = stm.executeQuery("SELECT * FROM issue");
//                while(rs.next()){
//                    String summary,description,issue_id;
//                    issue_id = rs.getString("issue_id");
//                    summary = rs.getString("summary");
//                    description = rs.getString("description");
//                    for(Vertex v : graph.adjVertices.keySet()){
//                        if(v.Name.compareTo(issue_id)==0 && v.type.compareTo("issue_id")==0){
//                            graph.for_ngram = v;
//                            break;
//                        }
//                    }
//                    if(summary==null){
//                        summary=" ";
//                    }
//                    if(description==null){
//                        description=" ";
//                    }
//                    graph.Summary.put(graph.for_ngram,summary);
//                    graph.Description.put(graph.for_ngram,description);
//                }
//                Ngrams Ngrams = new Ngrams();
//                for(Vertex v : graph.adjVertices.keySet()){
//                    if(v.Name.compareTo("MRM-415")==0 && v.type.compareTo("issue_id")==0){
//                        graph.A=v;
//                    }
//                    else if(v.Name.compareTo("MRM-485")==0 && v.type.compareTo("issue_id")==0){
//                        graph.B=v;
//                    }
//                }
//
//                String s1 = graph.Summary.get(graph.A) + graph.Description.get(graph.A);
//                String s2 = graph.Summary.get(graph.B) + graph.Description.get(graph.B);
//                System.out.printf("ngrams   %s   %s\n",graph.A.Name,graph.B.Name);
//                Ngrams.Print_Ngrams(Ngrams.Ngrams(5,s1),Ngrams.Ngrams(5,s2));
                //=====N grams=====//
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
