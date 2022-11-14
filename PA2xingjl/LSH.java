package MinHash_LSH;

import java.util.*;

public class LSH {

    private ArrayList<ArrayList<Set<String>>> hashTable;
    private int r;
    private int bands;
    private int prime;

    public LSH(int[][] minHashMatrix, String[] docNames, int bands){
        int k = minHashMatrix.length; //number of permutation
        this.bands = bands;
        r = (int)Math.ceil(k/bands); //each band has r rows
        int numFiles = docNames.length;

        int[][] rows = new int[r][numFiles];
        hashTable = new ArrayList<ArrayList<Set<String>>>(); //build new hashtable for set of filenames

        prime = getPrime(numFiles);
        Random rand = new Random();
        int a = rand.nextInt(prime);
        int b = rand.nextInt(prime);

        for (int z = 0; z < prime; z++){
            hashTable.add(new ArrayList<Set<String>>());
            for (int x = 0; x < bands; x++){
                hashTable.get(z).add(new HashSet<String>());
            }
        }
        for (int i = 0; i < numFiles; i++){
            String file = docNames[i];
            ArrayList<Integer> seperatedSig = new ArrayList<Integer>();
            int c = 0;
            for (int j = 0; j < k; j++){
                seperatedSig.add(minHashMatrix[j][i]);
                if(seperatedSig.size() == r){
                    int value = seperatedSig.hashCode();
                    seperatedSig.clear(); //reset the sepertedSig
                    value = Math.abs((a * value + b) % prime);
                    hashTable.get(value).get(c).add(file);
                    c++;
                }
            }
        }



    }

    private int getPrime(int numFiles) {
        boolean isFound = false;
        if(numFiles <= 1) {
            return 2;
        }
        int i = numFiles;//check the number after n
        while (!isFound) {
            i++;
            if(is_prime(i)) {
                isFound = true;//break the loop
            }
        }
        return i;
    }

    private boolean is_prime(int n) {
        if (n <= 1) {
            return false;
        }
        if (n==2||n==3) {
            return true;
        }
        if (n % 2 ==0||n%3 ==0) {
            return false;
        }
        for (int i = 5; i * i <=n; i = i +6) {
            if (n % i ==0) {
                return false;
            }
            if (n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }


    public ArrayList<String> nearDuplicatesOf(String docName){
        ArrayList<String> fileDup = new ArrayList<String>();

        for(int i = 0; i < bands; i++){
            for (int j = 0; j < prime; j++){
                if (hashTable.get(j).get(i).contains(docName)){
                    Iterator<String> iter = hashTable.get(j).get(i).iterator();
                    while (iter.hasNext()){
                        String str = iter.next();
                        if ((str.equals(docName)==false) && (fileDup.contains(str)==false)){
                            fileDup.add(str);
                        }
                    }
                }
            }
        }
        return fileDup;
    }



}
