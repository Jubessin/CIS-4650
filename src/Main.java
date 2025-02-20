/**
 * File Name: Main.java
 * The main class of the C- compiler.
 */
   
// import absyn.*;
import java.io.*;
   
class Main {
    public final static boolean SHOW_TREE = true;
    
    public static void main(String argv[]) {    
        try {
            // var parser = new Parser(new Lexer(new FileReader(argv[0])));

            // var result = (Absyn)(parser.parse().value);

            // if (SHOW_TREE && result != null) {
            //     System.out.println("The abstract syntax tree is:");
            //     var visitor = new ShowTreeVisitor();
            //     result.accept(visitor, 0); 
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
