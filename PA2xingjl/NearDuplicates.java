package MinHash_LSH;

import java.util.ArrayList;

public class NearDuplicates {

    private MinHashSimilarities sim;
    private LSH lsh;
    private double s;


    public NearDuplicates(String folder, int numPerm, double s){
        sim = new MinHashSimilarities(folder, numPerm);
        lsh = new LSH(sim.minHashMatrix(),sim.allDocs(), 16);
        this.s = s;
    }

    public String[] nearDuplicateDetector(String file){
        ArrayList<String> nearDup = lsh.nearDuplicatesOf(file);
        ArrayList<String> similarFile = new ArrayList<String>();
        for(int i = 0; i < nearDup.size(); i++){
            double simValue = sim.approximateJaccard(nearDup.get(i), file);
            if (simValue >= s){
                similarFile.add(nearDup.get(i));
            }
        }
        String[] res = new String[similarFile.size()];
        res = similarFile.toArray(res);
        return res;
    }
    public static void main(String[] args){
        NearDuplicates nearD = new NearDuplicates("/Users/jialin/Desktop/F17PA2", 400, 0.9);
        /*int[][] minHash = nearD.sim.minHashMatrix();
        for (int i = 0 ;i < minHash.length;i++){
            System.out.println(minHash[i][0]);
        }

         */
        /*String[] fileName = nearD.sim.allDocs();
        for (int i = 0; i < fileName.length; i++){
            System.out.println(fileName[i]);
        }

         */
        String[] res = nearD.nearDuplicateDetector("baseball0.txt");
        for (String dupe : res) {
            System.out.println(dupe);
        }
    }
}
