package absyn;

import java.util.*;

public class ProgramStack {

    public static ArrayList<Node> globalStack = new ArrayList<>();
    public static ArrayList<Node> frameStack = new ArrayList<>();

    public static int frameStackOffset = 0;
    public static int globalStackOffset = 0;

    public ProgramStack() {
    }
}
