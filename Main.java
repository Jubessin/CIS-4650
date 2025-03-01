/*
  File Name: Main.java
  Description: TODO
*/

import absyn.*;
import java.io.*;

public class Main {
    private static parser createParser(String args[]) throws Exception {
        var reader = new FileReader(args[0]);
        var lexer = new Lexer(reader);
        return new parser(lexer);
    }

    private static void processTree(Absyn tree) {
        var processor = new AbsynProcessor();

        System.out.println("Abstract syntax tree: ");
        System.out.println();

        tree.accept(processor, 0);
    }

    public static void main(String argv[]) {
        try {
            var parser = createParser(argv);
            var tree = (Absyn)parser.parse().value;

            processTree(tree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
