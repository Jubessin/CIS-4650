package absyn;

import java.io.*;
import java.util.Queue;
import java.util.LinkedList;

public class AbsynCodeGenerator implements AbsynVisitor {
    /** The set of predefined registers available for generation. */
    private final class Registers {
        /** Specifies the default register. */
        public static final int Default = 0;
        
        /** The program counter, pc. */
        public static final int ProgramCounter = 7;

        /** The global frame pointer, gp. */
        public static final int GlobalPointer = 6;

        /** The stack frame pointer, fp. */
        public static final int FramePointer = 5;

        /** Special register, ac. */
        public static final int AccumulatorA = 0;

        /** Special register, ac1. */
        public static final int AccumulatorB = 1;

        /**
         * Validates the register number.
         * @param register The register number to be validated.
         * @throws AssertionError if the register number is invalid.
         */
        public static void validateRegister(int register) {
            assert register == Default || 
                   register == ProgramCounter || 
                   register == GlobalPointer || 
                   register == FramePointer || 
                   register == AccumulatorA || 
                   register == AccumulatorB : 
                   "Invalid register: " + register;
        }
    }

    /** The set of register memory instructions available. */
    private final class MemoryInstruction {
        /** Store the value of {@code register1} into {@code register2}. */
        public static final String Store = "ST";

        /** Load the value of {@code register2} into {@code register1}. */
        public static final String Load = "LD";

        /** Load the address of {@code register2} into {@code register1}. */
        public static final String LoadAddress = "LDA";

        /** Load the {@code offset} value into {@code register1}. */
        public static final String LoadConstant = "LDC";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is equal 0. */
        public static final String JumpEqual = "JE";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is not equal to 0. */
        public static final String JumpNotEqual = "JNE";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is less than 0. */
        public static final String JumpLessThan = "JLT";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is less or equal to 0. */
        public static final String JumpLessEqual = "JLE";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is greater than 0. */
        public static final String JumpGreaterThan = "JGT";

        /** Jump to {@code offset} + value of {@code register2}, if {@code register1} is greater or equal to 0. */
        public static final String JumpGreaterEqual = "JGE";
        
        private static void validateInstruction(String instruction) {
            assert  instruction == Load || 
                    instruction == Store || 
                    instruction == JumpEqual || 
                    instruction == LoadAddress || 
                    instruction == LoadConstant || 
                    instruction == JumpNotEqual || 
                    instruction == JumpLessThan || 
                    instruction == JumpLessEqual || 
                    instruction == JumpGreaterThan || 
                    instruction == JumpGreaterEqual : 
                    "Invalid instruction: " + instruction;
        }
        
        /**
         * Appends an instruction to the builder using the format - {@code line: instruction register1, offset(register2) }.
         * @param line The line number of the instruction.
         * @param instruction The instruction to be printed.
         * @param register1 The first register to be printed.
         * @param offset The offset to be printed.
         * @param register2 The second register to be printed.
         */
        public static void print(int line, String instruction, int register1, int offset, int register2) {
            validateInstruction(instruction);
            Registers.validateRegister(register1);
            Registers.validateRegister(register2);
            builder.append(line + ":\t" + instruction + " " + register1 + "," + offset + "(" + register2 + ")\n");
        }

        /**
         * Increments the current line before calling {@link #print(int, String, int, int, int)}.
         * @param instruction The instruction to be printed.
         * @param register1 The first register to be printed.
         * @param offset The offset to be printed.
         * @param register2 The second register to be printed.
         */
        public static void print(String instruction, int register1, int offset, int register2) {
            print(++line, instruction, register1, offset, register2);
        }

        /**
         * Increments the current line before calling {@link #print(int, String, int, int, int)} using the default register and offset (0).
         * @param instruction The instruction to be printed.
         * @param register1 The first register to be printed.
         * @param offset The offset to be printed.
         * @param register2 The second register to be printed.
         */
        public static void print(String instruction) {
            print(++line, instruction, Registers.Default, Registers.Default, Registers.Default);
        }
    }

    /** The set of register (only) instructions available. */
    private final class RegisterInstruction {
        /** Stop program execution. */ 
        public static final String Halt = "HALT";
        
        /** Reads the value of {@code register1} as input. */ 
        public static final String Input = "IN";

        /** Writes the value of {@code register1} as output. */
        public static final String Output = "OUT";

        /** Computes the addition between the value of {@code register2} and {@code register3}, and stores in {@code register1}. */
        public static final String Add = "ADD";

        /** Computes the division between the value of {@code register2} and {@code register3}, and stores in {@code register1}. */
        public static final String Divide = "DIV";

        /** Computes the subtraction between the value of {@code register2} and {@code register3}, and stores in {@code register1}. */
        public static final String Subtract = "SUB";

        /** Computes the multiplication between the value of {@code register2} and {@code register3}, and stores in {@code register1}. */
        public static final String Multiply = "MUL";
  
        private static void validateInstruction(String instruction) {
            assert  instruction == Add || 
                    instruction == Halt || 
                    instruction == Input || 
                    instruction == Output || 
                    instruction == Divide || 
                    instruction == Subtract || 
                    instruction == Multiply :
                    "Invalid instruction: " + instruction;
        }
        
        // TODO: Test this method.
        /**
         * Appends an instruction to the builder using the format - {@code line: instruction register1, register2, register3 }.
         * @param line The line number of the instruction.
         * @param instruction The instruction to be printed.
         * @param registers The registers to be printed.
         */
        public static void print(int line, String instruction, int... registers) {
            validateInstruction(instruction);
            
            builder.append(line + ":\t" + instruction + " ");

            int i = 0;
            for (var register : registers) {
                Registers.validateRegister(register);
                builder.append(register + ",");
                ++i;
            }

            for (; i < 3; ++i) {
                builder.append(Registers.Default + ",");
            }

            builder.deleteCharAt(builder.length() - 1); // Remove the last comma.
            builder.append('\n');
        }
    
        /**
         * Increments the current line before calling {@link #print(int, String, int, int, int)}.
         * @param instruction The instruction to be printed.
         * @param registers The registers to be printed.
         */
        public static void print(String instruction, int... registers) {
            print(++line, instruction, registers);
        }
    }
    
    private static int line = -1;
    
    private static final StringBuilder builder = new StringBuilder();
    private static final Queue<Integer> sections = new LinkedList<>();

    private static void endSection() {
        var start = sections.remove();
        MemoryInstruction.print(start, MemoryInstruction.LoadAddress, Registers.ProgramCounter, line - start, Registers.ProgramCounter); // Jump to the start of the function?
    }

    private static void beginSection() {
        sections.add(++line);
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
        beginSection();

        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, -1, Registers.FramePointer);     // Store the return address

        // TODO: where/how to process parameters?

        dec.body.accept(this, level + 1, isAddress);

        MemoryInstruction.print(MemoryInstruction.Load, Registers.ProgramCounter, -1, Registers.FramePointer); // Load the return address, and return to caller.

        endSection();
    }

    @Override
    public void visit(IfExp exp, int level, boolean isAddress) {
        if (!(exp.body instanceof NilExp)) {
            beginSection();
        }

        exp.test.accept(this, level, isAddress);
        exp.body.accept(this, level, isAddress);

        if (!(exp._else instanceof NilExp)) {
            exp._else.accept(this, level, isAddress);
        }
        
        if (!(exp.body instanceof NilExp)) {
            endSection();
        }
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
        if (!(exp.body instanceof NilExp)) {
            beginSection();
        }

        exp.test.accept(this, level, isAddress);
        exp.body.accept(this, level, isAddress);

        if (!(exp.body instanceof NilExp)) {
            endSection();
        }
    }

    @Override
    public void visit(AssignExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(ReturnExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(CompoundExp exp, int level, boolean isAddress) {
        for (var item : exp.exps.getFlattened()) {
            item.accept(this, level, isAddress);
        }
    }

    @Override
    public void visit(CallExp exp, int level, boolean isAddress) {

    }

    @Override
    public void visit(NameTy type, int level, boolean isAddress) {

    }

    @Override
    public void visit(DecList list, int level, boolean isAddress) {
        // Setup
        MemoryInstruction.print(MemoryInstruction.Load, Registers.GlobalPointer, Registers.Default, Registers.Default);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.FramePointer, Registers.Default, Registers.Default);
        MemoryInstruction.print(MemoryInstruction.Store);

        beginSection();

        // Input
        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, -1, Registers.FramePointer);
        RegisterInstruction.print(RegisterInstruction.Input);
        MemoryInstruction.print(MemoryInstruction.Load, Registers.ProgramCounter, -1, Registers.FramePointer);

        // Output
        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, -1, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.Load, Registers.AccumulatorA, -2, Registers.FramePointer);
        RegisterInstruction.print(RegisterInstruction.Output);
        MemoryInstruction.print(MemoryInstruction.Load, Registers.ProgramCounter, -1, Registers.FramePointer);

        endSection();

        for (var item : list.getFlattened()) {
            item.accept(this, level, isAddress);
        }
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
