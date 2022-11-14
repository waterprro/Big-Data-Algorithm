import java.util.ArrayList;
import java.util.Arrays;

public class QueryProcessor {


    public ArrayList<String> topKDoc(String query, int k){
        PositionalIndex test = new PositionalIndex("/Users/jialin/Desktop/COM S 435:535/Fall 2020/IR");
        String[] file = test.getFileNames();
        String[] res = new String[k];
        double[] resR = new double[k];
        double relevance;
        double max = 0;
        for (int i = 0 ;i < k; i++){
            relevance = test.Relevance(query,file[i]);
            res[i] = file[i];
            resR[i] = relevance;
        }

        for (int j = k; j < test.getNumFiles(); j++){
            relevance = test.Relevance(query, file[j]);
            int minIndex = getMinIndex(resR);
            if (relevance > resR[minIndex]) {
                res[minIndex] = file[j];
                resR[minIndex] = relevance;
            }
        }
        ArrayList<String> arrayRes = new ArrayList<String>();
        for (int i = 0; i < k; i++){
            arrayRes.add(res[i]);
        }
        return arrayRes;
    }

    private int getMinIndex(double[] resR) {
        double min =100;
        int i = 0;
        for (int j = 0; j < resR.length; j++){
            if (resR[j] < min){
                min = resR[j];
                i = j;
            }
        }
        return i;
    }
}
