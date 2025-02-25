/*
  File Name: Main.java
  To Build: 
  After the Scanner.java, tiny.flex, and tiny.cup have been processed, do:
    javac Main.java
  
  To Run: 
    java -classpath ~/Projects/java-cup-11b.jar:. Main gcd.tiny

  where gcd.tiny is an test input file for the tiny language.
*/
   
import absyn.*;
import java.io.*;
   
class Main {
    public final static boolean SHOW_TREE = true;
    
    public static void main(String argv[]) {    
        try {
            var parser = new parser(new Lexer(new FileReader(argv[0])));

            var result = (Absyn)(parser.parse().value);

            if (SHOW_TREE && result != null) {
                System.out.println("The abstract syntax tree is:");
                // var visitor = new ShowTreeVisitor();
                // result.accept(visitor, 0); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
