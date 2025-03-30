package absyn;

import java.io.*;

public class AbsynCodeGenerator implements AbsynVisitor {
    // The set of predefined registers available for generation.
    private class Registers {
        // Specifies the default register.
        public static final int Default = 0;
        
        // The program counter, pc.
        public static final int ProgramCounter = 7;

        // The global frame pointer, gp.
        public static final int GlobalPointer = 6;

        // The stack frame pointer, fp.
        public static final int FramePointer = 5;

        // Special register, ac.
        public static final int AccumulatorA = 0;

        // Special register, ac1.
        public static final int AccumulatorB = 1;
    }

    // The set of instructions available for generation.
    private class Instructions {
        /************************** Format: instruction r1, r2, r3 **************************/

        // Stop program execution.
        public static final String Halt = "HALT";
        
        // Reads the value of r1 as input.
        public static final String Input = "IN";

        // Writes the value of r1 as output.
        public static final String Output = "OUT";

        // Computes the addition between the value of r2 and r3, and stores in r1.
        public static final String Add = "ADD";

        // Computes the division between the value of r2 and r3, and stores in r1.
        public static final String Divide = "DIV";

        // Computes the subtraction between the value of r2 and r3, and stores in r1.
        public static final String Subtract = "SUB";

        // Computes the multiplication between the value of r2 and r3, and stores in r1.
        public static final String Multiply = "MUL";

        /************************** Format: instruction r1, offset(r2) **************************/

        public static final String Store = "ST";

        // Load the value of r2 into r1. 
        public static final String Load = "LD";

        // Load the address of r2 into r1.
        public static final String LoadAddress = "LDA";

        // Load the offset value into r1.
        public static final String LoadConstant = "LDC";

        // Jump to offset + value of r2, if r1 is equal 0.
        public static final String JumpEqual = "JE";

        // Jump to offset + value of r2, if r1 is not equal to 0.
        public static final String JumpNotEqual = "JNE";

        // Jump to offset + value of r2, if r1 is less than 0.
        public static final String JumpLessThan = "JLT";

        // Jump to offset + value of r2, if r1 is less or equal to 0.
        public static final String JumpLessEqual = "JLE";

        // Jump to offset + value of r2, if r1 is greater than 0.
        public static final String JumpGreaterThan = "JGT";

        // Jump to offset + value of r2, if r1 is greater or equal to 0.
        public static final String JumpGreaterEqual = "JGE";
    }

    private static int line;

    private static final StringBuilder builder = new StringBuilder();

    private static void printRegisterInstruction(int line, String instruction) {
        printRegisterInstruction(line, instruction, Registers.Default, Registers.Default, Registers.Default);
    }

    private static void printRegisterInstruction(int line, String instruction, int register) {
        printRegisterInstruction(line, instruction, register, Registers.Default, Registers.Default);
    }

    private static void printRegisterInstruction(int line, String instruction, int register1, int register2, int register3) {
        builder.append(line + ":\t" + instruction + " " + register1 + "," + register2 + "," + register3 + "\n");
    }

    private static void printMemoryInstruction(int line, String instruction, int register1, int offset, int register2) {
        builder.append(line + ":\t" + instruction + " " + register1 + "," + offset + "(" + register2 + ")\n");
    }

    public void serialize(String file) throws FileNotFoundException, UnsupportedEncodingException {
        try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".tm"), "UTF-8")) {
            writer.println(builder.toString());
        }
    }

    @Override
    public void visit(SimpleDec dec, int level, boolean isAddress) {

    }

    @Override
    public void visit(ArrayDec dec, int level, boolean isAddress) {

    }

    @Override
    public void visit(FunctionDec dec, int level, boolean isAddress) {

    }

    @Override
    public void visit(IfExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(OpExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(IntExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(NilExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(VarExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(BoolExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(WhileExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(AssignExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(ReturnExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(CompoundExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(CallExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(NameTy type, int level, boolean isAddress) {

    }

    @Override
    public void visit(DecList list, int level, boolean isAddress) {
        printMemoryInstruction(
            line++, Instructions.Load, Registers.GlobalPointer, Registers.Default, Registers.Default
        );
        printMemoryInstruction(
            line++, Instructions.LoadAddress, Registers.FramePointer, Registers.Default, Registers.Default
        );
        printRegisterInstruction(
            line++, Instructions.Store
        );

        // TODO: accept
    }

    @Override
    public void visit(ExpList list, int level, boolean isAddress) {

    }

    @Override
    public void visit(VarDecList list, int level, boolean isAddress) {

    }

    @Override
    public void visit(IndexVar var, int level, boolean isAddress) {

    }

    @Override
    public void visit(SimpleVar var, int level, boolean isAddress) { 

    }
}