/*
* File Name: CM.java
* Description: Entrypoint of the C- compiler. 
*/

import absyn.*;
import java.io.*;

public class CM {
    private static String inputFile;
    private static boolean _processTree;

    private static String PROGRAM_USAGE = 
    "java -cp <cup_jar_path> CM -a <input_file>" +
    "\n\t-a             (Optional) Processes and displays the abstract syntax tree for the input file" +
    "\n\tinput_file     The file to compile, with the .cm extension";
    
    private static parser createParser(String args[]) throws Exception {
        var reader = new FileReader(inputFile);
        var lexer = new Lexer(reader);
        return new parser(lexer);
    }

    private static void processTree(Absyn tree) {
        if (!_processTree)
            return;
        
        var processor = new AbsynProcessor();

        System.out.println();
        System.out.println("Abstract syntax tree: ");

        tree.accept(processor, 0);
    }

    private static void readCommandLineArguments(String args[]) throws IllegalArgumentException, FileNotFoundException {
        if (args.length == 0)
            throw new IllegalArgumentException("The CM compiler requires a valid input file.");

        for (var arg : args) {
            var _arg = arg.trim();

            if (_arg.equals("-a")) {
                _processTree = true;
                continue;
            }

            if (inputFile == null && _arg.endsWith(".cm")) {
                inputFile = _arg;
                continue;
            }

            System.err.println("Unrecognized argument: " + _arg);
        }
    
        if (inputFile == null)
            throw new IllegalArgumentException("The CM compiler requires a valid input file.");

        var file = new File(inputFile);
        if (!file.exists())
            throw new FileNotFoundException("Cannot find the specified input file at path: " + inputFile);
    }

    public static void main(String argv[]) {
        try {
            readCommandLineArguments(argv);

            var parser = createParser(argv);
            var tree = (Absyn)parser.parse().value;

            processTree(tree);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println(PROGRAM_USAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
