import java.util.ArrayList;

public class Post {

    private String fileName;
    private int fileIndex;
    private ArrayList<Integer> pos;


    public Post(int fileIndex, String fileName , ArrayList<Integer> pos){
        this.fileName = fileName;
        this.fileIndex = fileIndex;
        this.pos = pos;
    }

    public int getSize(){
        return pos.size();
    }

    public void add(int position){
        pos.add(position);
    }

    public String getFileName() {
        return fileName;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public ArrayList<Integer> getPos() {
        return pos;
    }
}
