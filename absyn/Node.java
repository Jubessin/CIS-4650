package absyn;

public class Node {

    public Dec dec;
    // this will correspond to offsets, and line numbers
    public int level;
    public String name;

    public Node(String name, Dec dec, int level) {
        this.dec = dec;
        this.name = name;
        this.level = level;
    }
}
