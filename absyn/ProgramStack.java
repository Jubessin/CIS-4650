package absyn;

import java.util.*;

public class ProgramStack {

    public ArrayList<Node> globalStack;
    public ArrayList<Node> frameStack;

    public ProgramStack() {
        this.globalStack = new ArrayList<>();
        this.frameStack = new ArrayList<>();
    }
}
