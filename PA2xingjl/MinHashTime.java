package MinHash_LSH;

public class MinHashTime {

    public static void timer(String folder, int numPerm){
        //time to build constructor
        long begin = System.currentTimeMillis();
        MinHashSimilarities sim = new MinHashSimilarities(folder, numPerm);
        long finish = System.currentTimeMillis();
        System.out.println("Time to construct the minHashMatrix: "+ (finish-begin)+ " ms.");

        //time to compute the exact Jaccard Similarity
        String[] fileNames = sim.getFileNames();
        begin = System.currentTimeMillis();
        for (int i = 0; i < fileNames.length; i++) {
            for (int j = i + 1; j < fileNames.length; j++) {
                double exact = sim.exactJaccard(fileNames[i], fileNames[j]);
            }
        }
        finish = System.currentTimeMillis();
        System.out.println("Time to compute the exact Jaccard Similarity: "+ (finish-begin)+ " ms.");

        //time to compute the MinHashMatrix and use this to estimate Jaccard Similarity
        begin = System.currentTimeMillis();
        for (int i = 0; i < fileNames.length; i++) {
            for (int j = i + 1; j < fileNames.length; j++) {
                double appro = sim.approximateJaccard(fileNames[i], fileNames[j]);
            }
        }
        finish = System.currentTimeMillis();
        System.out.println("Time to compute approximate Jaccard Similarity: "+ (finish-begin)+ " ms");


    }

    public static void main(String[] args){
        timer("/Users/jialin/Desktop/space", 600);
    }
}
