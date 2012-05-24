/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package in.ac.iitb.wikispotter.trie;


import it.unimi.dsi.logging.ProgressLogger;
import it.unimi.dsi.webgraph.labelling.ArcLabelledImmutableGraph;
import it.unimi.dsi.webgraph.labelling.Label;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author pararth
 */
public class SearchGraph {
    
    public static String dir_path = "/home/ashish/dbfiles/";
    
    // This method takes a Label array and returns the corresponding labels in the form of an integer array
    public static int[] get_intlabels(Label[] labelstream,int m ) throws Exception{
        int successorlabels[] = new int[m];
        for(int i=0;i<m;i++){
            String S = labelstream[i].toString();
            char ch1 = ':';
            char ch2 = '(';
            int begin = S.indexOf(ch1);
            int end = S.indexOf(ch2);
            String stringint = S.substring(begin+1, end-1);
            successorlabels[i] = Integer.parseInt(stringint);
        }
        return successorlabels;
    }
    
    
    /*This method takes in an ArcLabelledImmutableGraph graph in which the string is to be searched,
     * a String S to be searched and a boolean varaible to determine what sort of output is expected.
     *
     * The zeroth node has no parent node, while all other nodes have their parent node as theor first successor.
     * To find any string, we start from the zeroth node and continue to search from there just as in a normal prefix tree.
     * The only point to keep in mind is that the first successor of every node except the zeroth node is the parent node of this
     * node under consideration. The corresponding labels are checked to match the ascii value with the required value.
     *
     *
     */
    
    public static int full_match(ArcLabelledImmutableGraph graph,String S,boolean method) throws Exception{
        
        final int start = 0;
        final int stop = S.length();
        int curr = 0;
        int nodevalue;
        int charint;
        
        for( int i = start; i < stop; i++ ) {
            int m = graph.outdegree(curr);
            Label labelstream[] = graph.labelArray(curr);
            int successorlabels[] = get_intlabels(labelstream,m);
            charint = S.charAt(i);
            if (charint>64 & charint <91)
                charint = charint +32;
            else if(charint==32)
                charint =95;
            boolean match = false;
            int searchstart;
            if (curr==0)
                searchstart = 0;
            else
                searchstart = 1;
            int nextnode=0;
            for (int j=searchstart;j<m & match==false;j++){
                nodevalue =successorlabels[j];
                //System.out.println("The label returned is "+nodevalue);
                if (nodevalue>=65 & nodevalue<=90)
                    nodevalue+=32;
                if (charint == nodevalue){
                    match = true;
                    nextnode = j;
                }
            }
            if (match == false)
                return 0;
            int successors[];
            successors = graph.successorArray(curr);
            curr = successors[nextnode];
            
        }
        return curr;
    }
    
    public static ArrayList<String> DFS(ArcLabelledImmutableGraph graph,int currNode) throws Exception
    {
        
        ArrayList<String> result=new ArrayList<String>();
        int outDg = graph.outdegree(currNode);
        int searchStart=0;
        if(currNode!=0)searchStart=1;
        if(currNode!=0 && outDg==1)
        {
            //leaf node
            result.add("");
            return result;
        }
        Label labelstream[] = graph.labelArray(currNode);
        int successorlabels[] = get_intlabels(labelstream,outDg);
        for(int i=searchStart;i<outDg;++i)
        {
            int edgeLabel=successorlabels[i];
            
            //recursion
            ArrayList<String> tempResults=DFS(graph,graph.successorArray(currNode)[i]);
            ArrayList<String> resultsHolder=new ArrayList<String>();
            for(int j=0;j<tempResults.size();++j)
            {
                StringBuilder temp=new StringBuilder();
                String currStr=tempResults.get(j);
                if(graph.outdegree(graph.successorArray(currNode)[i])==1)
                {
                    temp.append("\t");
                    temp.append(edgeLabel);
                }
                else{
                    
                    if(edgeLabel>=65 && edgeLabel<=90)
                        temp.append((char)(edgeLabel+32));
                    else
                        temp.append((char)edgeLabel);
                    temp.append(currStr);
                }
                resultsHolder.add(j, temp.toString());
            }
            result.addAll(resultsHolder);
            
        }
        return result;
    }
    
    
    //Go to the node for which the path from root to itself has the label S. And return all the leaf nodes under it.
    public static ArrayList<String> getPrefixMatches(ArcLabelledImmutableGraph graph,String S) throws Exception
    {
        ArrayList<String> result=new ArrayList<String>();
        final int start = 0;
        final int stop = S.length();
        int curr = 0;
        int nodevalue;
        int charint;
        
        for( int i = start; i < stop; i++ ) {
            int m = graph.outdegree(curr);
            Label labelstream[] = graph.labelArray(curr);
            int successorlabels[] = get_intlabels(labelstream,m);
            charint = S.charAt(i);
            if (charint>64 & charint <91)
                charint = charint +32;
            else if(charint==32)
                charint =95;
            boolean match = false;
            int searchstart;
            if (curr==0)
                searchstart = 0;
            else
                searchstart = 1;
            int nextnode=0;
            for (int j=searchstart;j<m & match==false;j++){
                nodevalue =successorlabels[j];
                //System.out.println("The label returned is "+nodevalue);
                if (nodevalue>=65 & nodevalue<=90)
                    nodevalue+=32;
                if (charint == nodevalue){
                    match = true;
                    nextnode = j;
                }
            }
            if (match == false)
                return null;
            int successors[];
            successors = graph.successorArray(curr);
            curr = successors[nextnode];
        }
        //now return all the descendants of curr. DFS is used here.
        if(curr!=0)
        {
            result=DFS(graph,curr);
            ArrayList<String> toReturn=new ArrayList<String>();
            
            for(int k=0;k<result.size();++k)
            {
                StringBuilder temp=new StringBuilder();
                temp.append(S);
                temp.append(result.get(k));
                toReturn.add(temp.toString());
            }
            return toReturn;
        }
        //append S at the beginning, for each string in "result"
        
        return null;
    }
    
    /*This method is used to load all the graph files into the memory of the computer. This takes the name of the files from the
     * file log.txt generated in labelled_graphgen
     *
     */
    
    public static ArcLabelledImmutableGraph[] graphloader()throws Exception{
        ArrayList<String> namelist = new ArrayList<String>();
        //ProgressLogger plist = new ProgressLogger();
        String basename,line,filename;
        FileInputStream fstream = new FileInputStream(dir_path + "log.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        while((line = br.readLine())!=null){
            String p[] = line.split("\t");
            filename  = p[0];
            int j = filename.indexOf(".");
            basename = filename.substring(0, j);
            //ProgressLogger pl = new ProgressLogger();
            //ArcLabelledImmutableGraph graph = ArcLabelledImmutableGraph.load(basename,pl);
            namelist.add(basename);
        }
        namelist.trimToSize();
        int n=namelist.size();
        ArcLabelledImmutableGraph GraphArray[] = new ArcLabelledImmutableGraph[n];
        for(int i=0 ;i<n ; i++){
            ProgressLogger pl = new ProgressLogger();
            GraphArray[i] = ArcLabelledImmutableGraph.load(namelist.get(i), pl);
            System.out.println("Loaded graph number "+i+" successfully");
        }
        return GraphArray;
    }
    
    
    /* This method is used to laod the information about the first 2 characters of the last word encoded in a particular file.
     *
     * This is useful in locating the particular graph file in which the required String may be present and should be searched.
     *
     */
    
    
    
    public static int[][] get_graph_boundaries()throws Exception{
        ArrayList<int []> graphbounds= new ArrayList<int []>();
        FileInputStream fstream = new FileInputStream(dir_path + "log.txt");
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while((line = br.readLine())!=null){
            String p[] = line.split("\t");
            graphbounds.add(new int[]{Integer.parseInt(p[1]),Integer.parseInt(p[2])});
        }
        graphbounds.trimToSize();
        int n=graphbounds.size();
        int graphends[][] = new int[n][2];
        for(int i=0;i<n;i++)
            graphends[i] = graphbounds.get(i);
        return graphends;
    }
    
    
    /* This method performs a binary search to find out the partilcar file in which the String should be searched for
     * availability. It takes in the ascii value of the first and the second characters of the string to be searched, along the
     * data given by the graph_boundaries
     *
     */
    
    public static int find_file(int firstchar, int secondchar, int n, int[][] graphbounds)throws Exception{
        //int filenumber;
        int hi=n-1,mid;
        int low=0;
        while(hi>low){
            mid = (hi+low)/2;
            if(firstchar>graphbounds[mid][0])//string definitely not in any file less than or equal to number mid
                low = mid+1;
            else if(firstchar<graphbounds[mid][0]){//string definitely not present in any file with number greater than mid
                if(mid==0)
                    return mid;
                else if(firstchar>graphbounds[mid-1][0])
                    return mid;
                else if(firstchar<graphbounds[mid-1][0])//string definitely not in mid
                    hi = mid-1;
                else if(firstchar==graphbounds[mid-1][0])//string can be in mid, as well as in any file lower than mid
                    hi = mid;
            }
            else if(firstchar==graphbounds[mid][0]){
                if(secondchar>graphbounds[mid][1])//string not present in mid or lower
                    low = mid+1;
                else if (secondchar<graphbounds[mid][1]){
                    if (mid==0)
                        return mid;
                    else if(firstchar>graphbounds[mid-1][0])
                        return mid;
                    else if(firstchar==graphbounds[mid-1][0]){//string definitely not in mid or higher
                        if(secondchar>graphbounds[mid-1][1])
                            return mid;
                        else if(secondchar<graphbounds[mid-1][1])
                            hi = mid-1;
                        else
                            return mid-1;
                    }
                }
                else if(secondchar==graphbounds[mid][1])
                    return mid;
            }
            
        }
        return hi;
    }
    
    /* This method is the one to be called by any external applcation for searching a string in the database. This basically finds
     * out the exact file which can be expected to contain the string by using the method find_file, and then calls a method to
     * search for the same in that file. This takes as input, an array of graph files, the string to be searched and the graphbounds
     * given by graph_boundaries.
     *
     */
    
    public static int find_string(ArcLabelledImmutableGraph[] graphlist, String str, int[][] graphbounds) throws Exception{
        int n = graphlist.length;
        //ProgressLogger pl1 = new ProgressLogger();
        ArcLabelledImmutableGraph graph = graphlist[n-1];
        int firstchar = (int)str.charAt(0);
        if(firstchar>=97 && firstchar<=122)
            firstchar -= 32;
        int secondchar =0;
        if(str.length()>1){
            secondchar = str.charAt(1);
            if(secondchar>=65 && secondchar<=90)
                secondchar += 32;
        }
        int q = find_file(firstchar, secondchar, n, graphbounds);
        graph = graphlist[q];
        int ans = SearchGraph.full_match(graph,str ,true);
        if (ans==0)
            System.out.println("The string was searched in graph number "+q+" and string is not present");
        else
            System.out.println("The string was searched in graph number "+q+" and was found at it's "+ans+"th node");
        return ans;
    }
    /*
     * Finds out the exact file in which to search for the prefix and then returns all the page titles which have str as a prefix
     */
    public static HashMap<Integer,String> prefixMatch(ArcLabelledImmutableGraph[] graphlist, String str, int[][] graphbounds) throws Exception
    {
        int n = graphlist.length;
        ArcLabelledImmutableGraph graph = graphlist[n-1];
        int firstchar = (int)str.charAt(0);
        if(firstchar>=97 && firstchar<=122)
            firstchar -= 32;
        int secondchar =0;
        if(str.length()>1){
            secondchar = str.charAt(1);
            if(secondchar>=65 && secondchar<=90)
                secondchar += 32;
        }
        int q = find_file(firstchar, secondchar, n, graphbounds);
        System.out.println("searching in file: "+q);
        graph = graphlist[q];
        ArrayList<String> result= SearchGraph.getPrefixMatches(graph,str);
        HashMap<Integer,String> toReturn=new HashMap<Integer, String>();
        if(result==null)return null;
        for(int i=0;i<result.size();++i)
        {
            String titleID=result.get(i);
            String parts[]=titleID.split("\t");
            int id=Integer.parseInt(parts[1]);
            toReturn.put(id, parts[0]);
        }
        return toReturn;
    }
    
    private static ArcLabelledImmutableGraph[] graphlist = null;
    private static int graphbounds[][] = null;
    
    public static HashMap<Integer, String> search(String str) {
        // TODO code application logic here
        try {
            if (graphlist == null){
                graphlist = SearchGraph.graphloader();
                System.out.println("Loaded the graphs");
            }
            if (graphbounds == null){
                graphbounds = SearchGraph.get_graph_boundaries();
                System.out.println("Loaded the graphbounds");
            }
            
            //find the matching titles. Call the function prefixMatch.
            return SearchGraph.prefixMatch(graphlist,str,graphbounds);
        } catch (Exception e){
            System.out.println("error: " + e);
            return null;
        }
    }
    
    
    /*This shows how exactly to use the search functions made above in order to fund out a string.
     *
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        if (graphlist == null){
            graphlist = SearchGraph.graphloader();
            System.out.println("Loaded the graphs");
        }
        if (graphbounds == null){
            graphbounds = SearchGraph.get_graph_boundaries();
            System.out.println("Loaded the graphbounds");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = in.readLine();
        //find the matching titles. Call the function prefixMatch.
        HashMap<Integer,String> result=SearchGraph.prefixMatch(graphlist,str,graphbounds);
        if(result==null)
        {
            System.out.println("no matches found");
            return;
        }
        //print the obtained results.
        Set<Integer> keySet=result.keySet();
        Iterator<Integer> it=keySet.iterator();
        while(it.hasNext())
        {
            int currId=it.next();
            String currTitle=result.get(currId);
            System.out.println("Id: "+currId+" Title: "+currTitle);
        }
    }
}
