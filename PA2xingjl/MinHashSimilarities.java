package MinHash_LSH;

import java.util.Hashtable;

public class MinHashSimilarities {

    private MinHash min;
    private int[][] minHashMatrix;
    private int[][] termMatrix;
    private String[] fileNames;
    private Hashtable<String, Integer> fileTable;
    /*
        constructor for minHashSimilarities
        call constructor minHash to create instance of minHash and store matrices.
     */
    public MinHashSimilarities(String folder, int numPermutations) {
        min = new MinHash(folder, numPermutations);
        minHashMatrix = min.minHashMatrix();
        termMatrix = min.termDocumentMatrix();
        fileNames = min.allDocs();
        fileTable = min.getFileSet();

    }
    public String[] getFileNames(){
        return min.allDocs();
    }

    public double exactJaccard(String file1, String file2){
        double sumMin = 0;
        double sumMax = 0;
        int file1Index = fileTable.get(file1)-1;
        int file2Index = fileTable.get(file2)-1;

        for (int i = 0; i < min.numTerm(); i++){
            int Min = Math.min(termMatrix[i][file1Index], termMatrix[i][file2Index]);
            int Max = Math.max(termMatrix[i][file1Index], termMatrix[i][file2Index]);
            sumMin += Min;
            sumMax += Max;
        }
        return sumMin/sumMax;
    }

    public double approximateJaccard(String file1, String file2){
        double count = 0;
        int file1Index = fileTable.get(file1)-1;
        int file2Index = fileTable.get(file2)-1;

        for (int i = 0; i < min.numPermutations(); i++){
            if (minHashMatrix[i][file1Index] == minHashMatrix[i][file2Index]){
                count ++;
            }
        }
        return count/min.numPermutations();
    }

    public int[] minHashSig(String fileName){
        int[] array = new int[min.numPermutations()];
        for (int i = 0; i < min.numPermutations(); i++){
            array[i] = minHashMatrix[i][fileTable.get(fileName)-1];
        }

        return array;
    }
    public String[] allDocs(){
        return fileNames;
    }

    public int[][] minHashMatrix(){
        return minHashMatrix;
    }

}
