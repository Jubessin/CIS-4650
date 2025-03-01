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

    private static void renderTree(Absyn tree) {
        var renderer = new AbsynRenderer();

        System.out.println("Abstract syntax tree: ");
        System.out.println();

        tree.accept(renderer, 0);
    }

    public static void main(String argv[]) {
        try {
            var parser = createParser(argv);
            var tree = (Absyn)parser.parse().value;

            renderTree(tree);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
