import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

public class PageRank {

    private int numVertices;
    private int numEdges;
    private int numIter;
    private double[] pList;
    private int[] inDegree;
    private Hashtable<String, Integer> vertex;
    private ArrayList<ArrayList<String>> edge;

    //e represents approximation for pagerank; beta represents teleportation
    PageRank(String fileName, double e, double beta){
        readFromFile(fileName);
        getPageRank(e, beta);
    }

    private void readFromFile(String fileName) {
        try{
            FileReader file = new FileReader(fileName);
            BufferedReader read = new BufferedReader(file);

            String str;
            String[] v;

            int outI;
            int inI;

            str = read.readLine();
            this.numEdges = 0;
            this.numVertices = Integer.parseInt(str);
            //System.out.println(numEdges);
            //initialize the data structure
            this.vertex = new Hashtable<>(numVertices);
            this.pList = new double[numVertices];
            Arrays.fill(pList, 1/(double)numVertices);
            this.inDegree = new int[numVertices];
            this.edge = new ArrayList<ArrayList<String>>(numVertices);
            for (int i = 0; i < numVertices; i++){
                edge.add(new ArrayList<String>());
            }

            while ((str = read.readLine()) != null){
                v = str.split("\\s+");
                if (vertex.containsKey(v[0]) == false){
                    outI = vertex.size();
                    vertex.put(v[0], outI);
                }
                else{
                    outI = vertex.get(v[0]);
                }
                if (vertex.containsKey(v[1]) == false){
                    inI = vertex.size();
                    vertex.put(v[1], inI);
                }
                else{
                    inI = vertex.get(v[1]);
                }
                numEdges++;
                edge.get(outI).add(v[1]);
                inDegree[inI]++;
                
            }
            //System.out.println(numEdges);


            read.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //compute the PageRank
    private void getPageRank(double e, double beta) {

        int index;
        int numOut;
        int iter = 0;
        boolean converged = false;
        double[] piPlusOne = new double[numVertices];

        while (converged == false){
            Arrays.fill(piPlusOne, (1-beta)/numVertices);
            for (int i = 0; i < numVertices; i++){
                numOut = edge.get(i).size();

                if( numOut == 0){
                    for (int j = 0; j < numVertices; j++ ){
                        piPlusOne[j] += (beta*pList[i]) / numVertices;
                    }
                }
                else{
                    for (String str : edge.get(i)){
                        index = vertex.get(str);
                        piPlusOne[index] += (beta*pList[i]) / numOut;
                    }
                }
            }
            double diff = difference(piPlusOne); //return difference between pi+1 and pi
            pList =  new double[numVertices];
            System.arraycopy(piPlusOne, 0, pList, 0, numVertices);
            iter++;
            if (diff <= e){
                this.numIter = iter;
                converged = true; //exit the while loop
            }
        }
    }

    private double difference(double[] piPlusOne) {
        double sum = 0.0;
        double singleDiff = 0.0;
        for (int i = 0; i < piPlusOne.length; i++){
            singleDiff = piPlusOne[i] - pList[i];
            sum += singleDiff * singleDiff;
        }
        double res = Math.sqrt(sum);
        return res;
    }

    //get a vertex as parameter and return its page rank
    public double pageRankOf(String vertex){
        int i = this.vertex.get(vertex);
        return pList[i];
    }

    //return number of edges of graph
    public int numEdges(){
        return numEdges;
    }

    public String[] topKPageRank(int k){
        double[] top = new double[k];
        int[] ints = new int[k];
        //initialize first k element in the list
        for( int i = 0; i < k; i++){
            top[i] = pList[i];
            ints[i] = i;
        }
        int minI = getMinI(top);

        for( int i = k; i < numVertices; i++){
            if (pList[i] > top[minI]) {
                top[minI] = pList[i];
                ints[minI] = i;
                minI = getMinI(top);
            }
        }

        int[] res = listTop(ints, k);
        String[] resS = new String[k];

        for (int j = 0; j < k; j++) {
            for (Map.Entry entry : vertex.entrySet()) {
                if (entry.getValue().equals(j)) {
                    resS[j] = (String) entry.getKey();
                }
            }
        }
        return resS;
    }

    private int[] listTop(int[] ints, int k) {
        int[] res = new int[k];
        double[] pageRankV = new double[k];

        for (int i = 0; i < ints.length; i++){
            pageRankV[i] = pList[ints[i]];
        }
        Arrays.sort(pageRankV);
        for (int i = 0; i < k; i ++){
            for (int j = 0; j < k ;j++){
                if (pageRankV[i] == pList[ints[j]]){
                    res[k-i-1] = ints[j];
                    System.out.println(pageRankV[i]);
                }
            }
        }

        return res;
    }

    private int getMinI(double[] top) {
        double min =100;
        int i = 0;
        for (int j = 0; j < top.length; j++){
            if (top[j] < min){
                min = top[j];
                i = j;
            }
        }

        return i;
    }

    public int getNumIter(){
        return numIter;
    }

    /**
     *

    public static void main(String[] args){
    PageRank test = new PageRank("/Users/jialin/Desktop/COM S 435:535/Fall 2020/WiKiSportsGraph.txt",0.01,0.85);
    String[] res;
    res = test.topKPageRank(10);
    for (String i :res){
        System.out.println(i);
    }

    System.out.println("End of list.");
    System.out.println(test.getNumIter());
    }
     *
     */
}
