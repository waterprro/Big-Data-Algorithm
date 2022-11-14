import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class PositionalIndex {

    private File folder;
    private String[] fileNames;
    private int numFiles;
    private Hashtable<String, DictionaryEntry> dictionary;

    public PositionalIndex(String folder){
        this.folder = new File(folder);
        dictionary = new Hashtable<String, DictionaryEntry>();

        File[] files = this.folder.listFiles();
        fileNames = this.folder.list();
        this.numFiles = files.length;
        for (int i = 0; i < numFiles; i++){
            if (files[i].isFile()){
                readFile(files[i]);
            }
        }
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public int getNumFiles() {
        return numFiles;
    }

    private void readFile(File file) {
        try{
            FileReader fileR = new FileReader(file);
            BufferedReader read = new BufferedReader(fileR);

            String str;
            String[] terms;
            int pos = 0;

            while ((str = read.readLine()) != null){
                str = str.toLowerCase();
                str = str.replaceAll("[,\"?\\]\\['{}:;()]","");
                terms = str.split("\\s+");

                for (int i = 0; i < terms.length; i++){
                    if ( terms[i].contains(".")){
                        try{
                            Double.parseDouble(terms[i]);
                        }
                        catch (NumberFormatException e){
                            terms[i] = terms[i].replaceAll("\\.", "");
                        }
                    }
                    if (dictionary.containsKey(terms[i]) == false){
                        DictionaryEntry de = new DictionaryEntry();
                        dictionary.put(terms[i], de);
                    }

                    DictionaryEntry de = dictionary.get(terms[i]);
                    de.addTp(file.getName(), pos);
                    pos++;
                }
            }
            read.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int termFrequency(String term, String Doc){
        return dictionary.get(term).getTf(Doc);
    }

    public int docFrequency(String term){
        return dictionary.get(term).getDf();
    }

    public String postingsList(String t){
        return dictionary.get(t).getPostList();
    }

    public double weight(String t, String d){
        int df;
        double res;
        if (dictionary.get(t) == null ){
            return 0;
        }
        else {
            df = dictionary.get(t).getDf();
        }
        double n = numFiles;
        res = (double)(log(1+termFrequency(t,d))*Math.log10(n/df));
        return res;
    }

    private double log(int i) {
        double res = 0;
        if (i == 1){
            return res;
        }
        else {
            res = (double) Math.log(i) / Math.log(2);
            return res;
        }
    }


    public double TPScore(String query, String doc){
        String[] terms = simplifyQuery(query);
        int len = terms.length;

        if (len == 1){
            return 0;
        }
        int dis = 0;
        String t1,t2;

        for (int i = 0 ; i < terms.length-1; i++){
            t1 = terms[i];
            t2 = terms[i+1];

            ArrayList<Integer> pos1, pos2;
            if (dictionary.containsKey(t1) || dictionary.containsKey(t2)){
                dis = 17;
            }
            else if ( (pos1 = dictionary.get(t1).getPosition(doc)) == null || ((pos2 = dictionary.get(t2).getPosition(doc)) == null)){
                dis += 17;
            }
            else{
                dis += findDistance(pos1, pos2);
            }
        }
        return  (double)1/dis;
    }

    private int findDistance(ArrayList<Integer> pos1, ArrayList<Integer> pos2) {
        int i2 = 0;
        int p2 = pos2.get(i2);
        int min = 17;

        for (int i = 0; i < pos1.size(); i++){
            int p1 = pos1.get(i);
            while (p1 > p2){
                i2 ++;
                if (i2 == pos2.size()){
                    min = 17;
                    return min;
                }
                p2 = pos2.get(i2);
            }
            int dis = p2 - p1;
            min = Math.min(min, dis);
        }
        return min;
    }

    private String[] simplifyQuery(String query) {
        query = query.toLowerCase();
        query = query.replaceAll("[,\"?\\]\\['{}:;()]","");
        String[] terms = query.split("\\s+");
        for (int i = 0; i < terms.length; i++){
            if (terms[i].contains(".")){
                try{
                    Double.parseDouble(terms[i]);
                }
                catch (NumberFormatException e){
                    terms[i] = terms[i].replaceAll("\\.", "");
                }
            }
        }
        return terms;
    }

    public double VSScore(String query, String doc){
        String[] terms = simplifyQuery(query);
        int tf = 0;

        for (String term: terms){
            if (dictionary.containsKey(term)){
                tf += dictionary.get(term).getTf(doc);
            }
        }
        if (tf == 0){
            return 0;
        }

        Hashtable<String, Double> queryW = queryWV(terms);
        Hashtable<String, Double> docW = docWV(doc);

        double dotRes = dotProduct(docW, queryW);
        double crossRes = crossProduct(docW, queryW);
        if (crossRes == 0){
            return 0;
        }
        return (double) dotRes/crossRes;
    }

    private double crossProduct(Hashtable<String, Double> docW, Hashtable<String, Double> queryW) {
        double docCross = 0;
        double queryCross = 0;
        double res;

        for (String key: queryW.keySet()){
            queryCross += queryW.get(key) * queryW.get(key);
        }

        for (String key: docW.keySet()){
            docCross += docW.get(key) * docW.get(key);
        }
        res = Math.sqrt(docCross)*Math.sqrt(queryCross);

        return res;
    }

    private double dotProduct(Hashtable<String, Double> docW, Hashtable<String, Double> queryW) {
        double dotRes = 0;
        for (String key: queryW.keySet()){
            if (docW.containsKey(key)){
                dotRes += queryW.get(key) * docW.get(key);
            }
        }
        return dotRes;
    }

    private Hashtable<String, Double> docWV(String doc) {
        File file = new File(folder, doc);
        Hashtable<String , Double> w = new Hashtable<String, Double>();
        try{
            FileReader fileR = new FileReader(file);
            BufferedReader read = new BufferedReader(fileR);

            String str;
            String[] terms;
            while ((str = read.readLine()) != null){
                str = str.toLowerCase();
                str = str.replaceAll("[,\"?\\]\\['{}:;()]", "");
                terms = str.split("\\s+");

                for (int i = 0; i < terms.length; i++){
                    if (terms[i].contains(".")){
                        try{
                            Double.parseDouble(terms[i]);
                        }
                        catch (NumberFormatException e){
                            terms[i] = terms[i].replaceAll("\\.", "");
                        }
                    }
                    if (w.containsKey(terms[i]) == false){
                        w.put(terms[i], weight(terms[i],doc));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return w;
    }

    private Hashtable<String, Double> queryWV(String[] terms) {
        Hashtable<String, Double> qw = new Hashtable<String , Double>();

        for (int i = 0; i < terms.length; i++){
            int fq = 1;
            for (int j = i+1; j < terms.length; j++){
                if (terms[i].equals(terms[j])){
                    fq++;
                }
            }
            double w;
            if (dictionary.get(terms[i]) == null){
                w = 0.0;
            }
            else{
                w = log(1 + fq) * Math.log10(((double) numFiles)/dictionary.get(terms[i]).getDf());
            }
            if (qw.containsKey(terms[i]) == false ){
                qw.put(terms[i], w);
            }
        }
        return qw;
    }

    public double Relevance(String query, String doc){
        double res = 0.6*TPScore(query,doc) + 0.4*VSScore(query, doc);
        return res;
    }
}
