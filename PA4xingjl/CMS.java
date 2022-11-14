import java.util.ArrayList;
import java.util.Random;

public class CMS {

    private int k; // k rows
    private int l; // l columns
    private int[][] cms; //2-D arrays with k rows and l columns
    private ArrayList<Integer> s; //stream
    private float epsilon, delta, q, r;
    private int[] a; //ax+b
    private int[] b; //ax+b
    private ArrayList<Integer> uniqueList;

    //constructor for cms
    public CMS(float epsilon, float delta, ArrayList<Integer> s, float q, float r){
        this.epsilon = epsilon;
        this.delta = delta;
        this.q = q;
        this.r = r;
        this.s = s;

        k = (int) Math.round(Math.log(1/delta));
        l = (int) Math.round(2/epsilon);
        l = findPrime(l);
        cms = new int[k][l];
        a = new int[k];
        b = new int[k];
        addHashF(k);

        uniqueList = new ArrayList<>();

        //write in the data structure
        for (int x : this.s){
            if (uniqueList.contains(x) == false){
                uniqueList.add(x);
            }
            for (int i = 0; i < k; i++){
                int hashV= 0;
                hashV = Math.abs(((a[i]*x) + b[i])% l);
                cms[i][hashV] ++;
            }
        }
    }

    public double approximateFrequency(int x){
        double res = 0.0;
        double min = Double.MAX_VALUE;
        //get the min value in the cms[i][hashV]
        for (int i = 0; i < k; i++){
            int hashV= 0;
            hashV = Math.abs(((a[i]*x) + b[i])% l);
            if (cms[i][hashV] < min){
                min = cms[i][hashV];
            }
        }
        res = min;
        return  res;
    }

    public int[] approximateHH(){
        ArrayList<Integer> HH = new ArrayList<>();
        int size = this.s.size();
        for (int x : uniqueList){
            double approximation = approximateFrequency(x);
            if (approximation >= q*size){
                HH.add(x);
            }
        }
        int[] res = new int[HH.size()];
        int i = 0;
        for (int x : HH){
            res[i] = x;
            i++;
        }
        return res;
    }

    private void addHashF(int k) {
        Random r = new Random();
        int A, B;
        for (int i = 0; i < k; i++) {
            A = r.nextInt(l);
            B = r.nextInt(l);
            a[i] = A;
            b[i] = B;
        }
    }


    private int findPrime(int l) {
        boolean isFound = false;
        if(l <= 1) {
            return 2;
        }
        int i = l;//check the number after n
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
}
