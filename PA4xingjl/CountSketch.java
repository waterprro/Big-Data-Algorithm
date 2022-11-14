import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CountSketch {

    private int k; // k rows
    private int l; // l columns
    private int[][] cms; //2-D arrays with k rows and l columns
    private ArrayList<Integer> s; //stream
    private float epsilon, delta, q, r;
    private int[] a; //ax+b
    private int[] b; //ax+b
    private int[] c; //g()function

    public CountSketch(float epsilon, float delta, ArrayList<Integer> s){
        this.epsilon = epsilon;
        this.delta = delta;
        this.s = s;

        k = (int) Math.round(Math.log(1/delta));
        l = (int) Math.round(3/(epsilon*epsilon));
        l = findPrime(l);
        cms = new int[k][l];
        a = new int[k];
        b = new int[k];
        c = new int[k];
        addHashF(k);

        for(int x: this.s){
            for (int i = 0; i < k; i++){
                int hashV= 0;
                hashV = Math.abs(((a[i]*x) + b[i])% l);
                if (c[i] == 0){
                    cms[i][hashV] -= 1;
                }
                else{
                    cms[i][hashV] += 1;
                }
            }
        }
    }


    public double approximateFrequency(int x){
        double res = 0.0;
        ArrayList<Double> array = new ArrayList<Double>();

        for (int i = 0; i < k; i++){
            int hashV= 0;
            hashV = Math.abs(((a[i]*x) + b[i])% l);
            if (c[i] == 0){
                array.add((double)(-1)*cms[i][hashV]);
            }
            else{
                array.add((double)cms[i][hashV]);
            }
        }
        Collections.sort(array);
        int size = array.size();
        if (size % 2 == 0){
            res = (array.get((size/2)-1)+array.get(size/2))/2;
        }
        else {
            res = array.get(size/2);
        }
        return res;
    }

    private void addHashF(int k) {
        Random r = new Random();
        int A, B, C;
        for (int i = 0; i < k; i++) {
            A = r.nextInt(l);
            B = r.nextInt(l);
            C = r.nextInt(2);//get 0 or 1
            a[i] = A;
            b[i] = B;
            c[i] = C;
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
