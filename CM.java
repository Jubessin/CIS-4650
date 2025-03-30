/*
* File Name: CM.java
* Description: Entrypoint of the C- compiler. 
 */

import absyn.*;
import java.io.*;

public class CM {

    private static String inputFile;
    private static boolean saveSyntaxTree;
    private static boolean saveSymbolTable;
    private static boolean saveAssemblyCode;

    private static String PROGRAM_USAGE
            = "java -cp <cup_jar_path> CM -a <input_file>"
            + "\n\t-a             (Optional) Saves the abstract syntax tree to a file"
            + "\n\t-s             (Optional) Saves the symbol table to a file"
            + "\n\t-c             (Optional) Saves generated assembly code"
            + "\n\tinput_file     The file to compile, with the .cm extension";

    private static parser createParser(String args[]) throws Exception {
        var reader = new FileReader(inputFile);
        var lexer = new Lexer(reader);
        return new parser(lexer);
    }

    private static void runBuilder(Absyn tree) throws Exception {
        var builder = new AbsynTreeBuilder();

        tree.accept(builder, 0);

        if (saveSyntaxTree) {
            builder.serialize(inputFile);
        }
    }

    private static void runAnalyzer(Absyn tree) throws Exception {
        var analyzer = new AbsynSemanticAnalyzer();

        tree.accept(analyzer, 0);

        if (saveSymbolTable) {
            analyzer.serialize(inputFile);
        }
    }

    private static void runGenerator(Absyn tree) throws Exception {
        if (!absyn.Error.getIsValid()) {
            return;
        }

        var generator = new AbsynCodeGenerator();

        tree.accept(generator, 0);

        if (saveAssemblyCode) {
            generator.serialize(inputFile);
        }
    }

    private static void readCommandLineArguments(String args[]) throws IllegalArgumentException, FileNotFoundException {
        if (args.length == 0) {
            throw new IllegalArgumentException("The CM compiler requires a valid input file.");
        }

        for (var arg : args) {
            var _arg = arg.trim();

            if (_arg.equals("-a")) {
                saveSyntaxTree = true;
                continue;
            }

            if (_arg.equals("-s")) {
                saveSymbolTable = true;
                continue;
            }

            if (_arg.equals("-c")) {
                saveAssemblyCode = true;
                continue;
            }

            if (inputFile == null && _arg.endsWith(".cm")) {
                inputFile = _arg;
                continue;
            }

            System.err.println("Unrecognized argument: " + _arg);
        }

        if (inputFile == null) {
            throw new IllegalArgumentException("The CM compiler requires a valid input file.");
        }

        var file = new File(inputFile);
        if (!file.exists()) {
            throw new FileNotFoundException("Cannot find the specified input file at path: " + inputFile);
        }
    }

    public static void main(String argv[]) {
        try {
            readCommandLineArguments(argv);

            var parser = createParser(argv);
            var tree = (Absyn) parser.parse().value;

            if (!parser.valid) {
                return;
            }

            runBuilder(tree);
            runAnalyzer(tree);
            runGenerator(tree);
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
