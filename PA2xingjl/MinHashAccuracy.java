package MinHash_LSH;

public class MinHashAccuracy {

    public static void accuracy(String folder, int numPerm, double error){
        MinHashSimilarities sim = new MinHashSimilarities(folder, numPerm);
        String[] fileNames = sim.getFileNames();
        int c = 0;

        for (int i = 0; i < fileNames.length; i++){
            for (int j = i+1; j < fileNames.length; j++){
                double exact = sim.exactJaccard(fileNames[i],fileNames[j]);
                double appro = sim.approximateJaccard(fileNames[i],fileNames[j]);
                if (Math.abs(exact-appro) > error){
                    c++;
                }
            }
        }
        System.out.println("Number of pairs that difference between exact and approximate larger than error "+ error+" = "+ c);

    }

    public static void main(String[] args){

        int[] numPerm = {400, 600, 800};
        double[] error = {0.04, 0.07, 0.09};
        for (int i = 0; i < numPerm.length; i++){
            System.out.println("Number of permutations is "+numPerm[i]);
            for (int j = 0 ; j < error.length; j++){
                accuracy("/Users/jialin/Desktop/space",numPerm[i] ,error[j]);
            }
        }

    }
}
