package MinHash_LSH;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;


/*

 */
public class MinHash {

    private String folder;
    private int numPermutations;
    private int numFile;
    private int[][] minHashMatrix;
    private int[][] termMatrix;
    private Integer[][] permutation;//randomly pick from {1,2,3...M} to {1,2,3...M}
    private int numTerm;
    private String[] fileNames;
    private Hashtable<String, Integer> termSet = new Hashtable<String, Integer>();
    private Hashtable<String, Integer> fileSet = new Hashtable<String, Integer>();

    /*
    constructor for the MinHash
     */
    public MinHash(String folder, int numPermutations){
        this.folder = folder;
        File files = new File(folder);
        this.numPermutations = numPermutations;
        fileNames = files.list();
        numFile = fileNames.length; // number of document
        fileSet = putFile(fileNames);
        termSet = preprocess(fileNames); //set up set U that terms appear in the document
        numTerm = termSet.size();

        permutation = buildPerm(numTerm, numPermutations);
        minHashMatrix = new int[numPermutations][numFile];
        termMatrix = new int[numTerm][numFile];
        termMatrix = fillTermMatrix();
        minHashMatrix = fillMinHashMatrix();

    }

    public Hashtable<String, Integer> putFile(String[] fileNames) {
        Hashtable<String, Integer> fileTable = new Hashtable<>(numFile);
        for (int i = 0; i < numFile; i++){
            fileTable.put(fileNames[i],fileTable.size()+1);
        }
        return fileTable;
    }

    public Hashtable<String, Integer> getFileSet(){
        return fileSet;
    }

    private int[][] fillMinHashMatrix() {
        int[][] matrix = new int[numPermutations][numFile];

        for (int i = 0; i < numFile; i++){ //compute each mins for one file at a time
            for (int j = 0; j < numPermutations; j++){ //compute one permutation for one file
                int tempMin=801;
                for(int z = 0; z < numTerm; z++){
                    if ((termMatrix[z][i]>0) && (tempMin > permutation[j][z])){ //the number of term that appears in the document
                        tempMin = permutation[j][z];
                    }
                }
                matrix[j][i] = tempMin;
            }
        }
        return matrix;
    }

    private int[][] fillTermMatrix() {
        int[][] matrix = new int[numTerm][numFile];
        try{
            for (int i = 0; i < numFile; i++){
                FileReader fileReader = new FileReader(folder+"/"+fileNames[i]);
                BufferedReader reader = new BufferedReader(fileReader);

                String str;
                String[] terms;
                while((str=reader.readLine())!= null) {
                    str = str.toLowerCase();
                    str = str.replaceAll("[,.;:']", "");
                    terms = str.split("\\s+");

                    for (int j = 0; j < terms.length; j++){
                        if ((terms[j].length() > 2) && (terms[j].equals("the")==false)){
                            matrix[termSet.get(terms[j])-1][i]++; //if one term in the document T(j-1)(i) plus one
                        }
                    }

                }

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }


        return matrix;
    }

    private Integer[][] buildPerm(int numTerm, int numPermutations) {
        ArrayList<Integer> shuffle = new ArrayList<>(numTerm);
        Integer[][] permList = new Integer[numPermutations][numTerm];

        for(int i = 1; i <= numTerm; i++){
            shuffle.add(i);//build set of {1,2,3,...,M}
        }

        for(int i = 0; i < numPermutations; i++){
            Collections.shuffle(shuffle);
            permList[i] = shuffle.toArray(permList[i]);
        }

        return permList;
    }

    private Hashtable<String, Integer> preprocess(String[] fileNames) {
        Hashtable<String, Integer> set = new Hashtable<String, Integer>();
        try{
            for(int i = 0;i < fileNames.length; i++){
                FileReader fileReader = new FileReader(folder+"/"+fileNames[i]);
                //System.out.println(fileNames[i]);
                BufferedReader reader = new BufferedReader(fileReader);
                String str;
                String[] terms;
                while((str=reader.readLine())!= null){
                    str = str.toLowerCase();
                    str = str.replaceAll("[,.;:']","");
                    terms = str.split("\\s+");
                    for(int j = 0; j < terms.length; j++){
                        if ((terms[j].length() > 2) && (terms[j].equals("the")==false) && (set.containsKey(terms[j])==false)){
                            set.put(terms[j], set.size()+1);
                        }
                    }
                }
                reader.close();

            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return set;
    }

    public String[] allDocs(){
        return fileNames;
    }

    public int[][] minHashMatrix(){
        return minHashMatrix;
    }

    public int[][] termDocumentMatrix(){
        return termMatrix;
    }

    public int numTerm(){
        return numTerm;
    }

    public int numPermutations(){
        return numPermutations;
    }

}
