import java.util.*;

public class DictionaryEntry {

    private int df;//number of documents that holds the term
    private Hashtable<String, Post> posting;

    DictionaryEntry(){
        posting = new Hashtable<String, Post>();
        df = 0;
    }

    public int getDf(){
        return df;
    }

    public int getTf(String file){
        int tf = 0;
        if (posting.get(file) == null){
            return tf;
        }
        else{
            tf = posting.get(file).getSize();
            return tf;
        }
    }
    /*
        get a position list for posting list
     */
    public ArrayList<Integer> getPosition(String file){
        ArrayList<Integer> pos = new ArrayList<>();
        if (posting.get(file) == null){
            return null;
        }
        else{
            pos = posting.get(file).getPos();
            return pos;
        }
    }

    public void addTp(String file, int pos){

        if (posting.containsKey(file) == false){
            ArrayList<Integer> temp = new ArrayList<Integer>();

            df++;
            Post post = new Post(df, file, temp);
            posting.put(file, post);
        }
        posting.get(file).add(pos);

    }

    public String getPostList(){
        Collection<Post> collection = posting.values();
        TreeSet<Post> posts = new TreeSet<Post>(collection);

        String res = "[";

        while (posting.isEmpty() == false){
            Post post = posts.pollFirst();
            String fileName = post.getFileName();
            String p = "<";
            p += fileName + ":";

            int numPos = posting.get(fileName).getSize();
            ArrayList<Integer> position = posting.get(fileName).getPos();

            for (int i = 0; i < numPos; i++){
                if (i + 1 == numPos){
                    p += position.get(i);
                }
                else{
                    p += position.get(i) + ",";
                }
            }
            res += p;
            p += ">,";
        }
        res += ">]";

        return res;
    }
}
