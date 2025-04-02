package absyn;

import java.io.*;
import java.util.*;

public class AbsynCodeGenerator implements AbsynVisitor {

    public ArrayList<FunctionDec> functions = new ArrayList<>();

    /**
     * The set of predefined registers available for generation.
     */
    private final class Registers {

        /**
         * Specifies the default register.
         */
        public static int Default = 0;

        /**
         * The program counter, pc.
         */
        public static int ProgramCounter = 7;

        /**
         * The global frame pointer, gp.
         */
        public static int GlobalPointer = 6;

        /**
         * The stack frame pointer, fp.
         */
        public static int FramePointer = 5;

        /**
         * Special register, ac.
         */
        public static int AccumulatorA = 0;

        /**
         * Special register, ac1.
         */
        public static int AccumulatorB = 1;

        /**
         * Validates the register number.
         *
         * @param register The register number to be validated.
         * @throws AssertionError if the register number is invalid.
         */
        public static void validateRegister(int register) {
            assert register == Default
                    || register == ProgramCounter
                    || register == GlobalPointer
                    || register == FramePointer
                    || register == AccumulatorA
                    || register == AccumulatorB :
                    "Invalid register: " + register;
        }
    }

    /**
     * The set of register memory instructions available.
     */
    private final class MemoryInstruction {

        /**
         * Store the value of {@code register1} into {@code register2}.
         */
        public static String Store = "ST";

        /**
         * Load the value of {@code register2} into {@code register1}.
         */
        public static String Load = "LD";

        /**
         * Load the address of {@code register2} into {@code register1}.
         */
        public static String LoadAddress = "LDA";

        /**
         * Load the {@code offset} value into {@code register1}.
         */
        public static String LoadConstant = "LDC";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is equal 0.
         */
        public static String JumpEqual = "JEQ";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is not equal to 0.
         */
        public static String JumpNotEqual = "JNE";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is less than 0.
         */
        public static String JumpLessThan = "JLT";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is less or equal to 0.
         */
        public static String JumpLessEqual = "JLE";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is greater than 0.
         */
        public static String JumpGreaterThan = "JGT";

        /**
         * Jump to {@code offset} + value of {@code register2}, if
         * {@code register1} is greater or equal to 0.
         */
        public static String JumpGreaterEqual = "JGE";

        private static void validateInstruction(String instruction) {
            assert instruction.equals(Load)
                    || instruction.equals(Store)
                    || instruction.equals(JumpEqual)
                    || instruction.equals(LoadAddress)
                    || instruction.equals(LoadConstant)
                    || instruction.equals(JumpNotEqual)
                    || instruction.equals(JumpLessThan)
                    || instruction.equals(JumpLessEqual)
                    || instruction.equals(JumpGreaterThan)
                    || instruction.equals(JumpGreaterEqual) :
                    "Invalid instruction: " + instruction;
        }

        /**
         * Appends an instruction to the builder using the format - {@code line: instruction register1, offset(register2)
         * }.
         *
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

            builder.append(line).append(":\t\t").append(instruction).append("\t\t").append(register1).append(",").append(offset).append("(").append(register2).append(")\n");
        }

        /**
         * Increments the current line before calling
         * {@link #print(int, String, int, int, int)}.
         *
         * @param instruction The instruction to be printed.
         * @param register1 The first register to be printed.
         * @param offset The offset to be printed.
         * @param register2 The second register to be printed.
         */
        public static void print(String instruction, int register1, int offset, int register2) {
            print(++line, instruction, register1, offset, register2);
        }

        /**
         * Increments the current line before calling
         * {@link #print(int, String, int, int, int)} using the default register
         * and offset (0).
         *
         * @param instruction The instruction to be printed.
         * @param register1 The first register to be printed.
         * @param offset The offset to be printed.
         * @param register2 The second register to be printed.
         */
        public static void print(String instruction) {
            print(++line, instruction, Registers.Default, Registers.Default, Registers.Default);
        }
    }

    /**
     * The set of register (only) instructions available.
     */
    private final class RegisterInstruction {

        /**
         * Stop program execution.
         */
        public static String Halt = "HALT";

        /**
         * Reads the value of {@code register1} as input.
         */
        public static String Input = "IN";

        /**
         * Writes the value of {@code register1} as output.
         */
        public static String Output = "OUT";

        /**
         * Computes the addition between the value of {@code register2} and
         * {@code register3}, and stores in {@code register1}.
         */
        public static String Add = "ADD";

        /**
         * Computes the division between the value of {@code register2} and
         * {@code register3}, and stores in {@code register1}.
         */
        public static String Divide = "DIV";

        /**
         * Computes the subtraction between the value of {@code register2} and
         * {@code register3}, and stores in {@code register1}.
         */
        public static String Subtract = "SUB";

        /**
         * Computes the multiplication between the value of {@code register2}
         * and {@code register3}, and stores in {@code register1}.
         */
        public static String Multiply = "MUL";

        @SuppressWarnings("StringEquality")
        private static void validateInstruction(String instruction) {
            assert instruction == Add
                    || instruction == Halt
                    || instruction == Input
                    || instruction == Output
                    || instruction == Divide
                    || instruction == Subtract
                    || instruction == Multiply :
                    "Invalid instruction: " + instruction;
        }

        // TODO: Test this method.
        /**
         * Appends an instruction to the builder using the format - {@code line: instruction register1, register2, register3
         * }.
         *
         * @param line The line number of the instruction.
         * @param instruction The instruction to be printed.
         * @param registers The registers to be printed.
         */
        public static void print(int line, String instruction, int... registers) {
            validateInstruction(instruction);

            builder.append(line).append(":\t\t").append(instruction).append("\t\t");

            int i = 0;
            for (var register : registers) {
                Registers.validateRegister(register);
                builder.append(register).append(",");
                ++i;
            }

            for (; i < 3; ++i) {
                builder.append(Registers.Default).append(",");
            }

            builder.deleteCharAt(builder.length() - 1); // Remove the last comma.
            builder.append('\n');
        }

        /**
         * Increments the current line before calling
         * {@link #print(int, String, int, int, int)}.
         *
         * @param instruction The instruction to be printed.
         * @param registers The registers to be printed.
         */
        public static void print(String instruction, int... registers) {
            print(++line, instruction, registers);
        }
    }

    private final class ProgramStack {

        public static ArrayList<Node> globalStack = new ArrayList<>();
        public static ArrayList<Node> frameStack = new ArrayList<>();

        public static int frameStackOffset = 0;
        public static int globalStackOffset = 0;

        public static Node find(String name) {
            for (var item : ProgramStack.frameStack) {
                if (item.name.equals(name)) {
                    return item;
                }
            }

            for (var item : ProgramStack.globalStack) {
                if (item.name.equals(name)) {
                    return item;
                }
            }

            return null;
        }
    }

    private static int line = -1;
    private static int mainFunctionAddress;

    private static final StringBuilder builder = new StringBuilder();
    private static final Stack<Integer> sections = new Stack<>();

    private static void endSection() {
        var start = sections.pop();
        MemoryInstruction.print(start, MemoryInstruction.LoadAddress, Registers.ProgramCounter, line - start, Registers.ProgramCounter); // Jump to the start of the function?
    }

    private static void beginSection() {
        sections.push(++line);
    }

    public void serialize(String file) throws FileNotFoundException, UnsupportedEncodingException {
        // Wrote this because the tm.c program doesn't seem to work with tabs (so I convert them into spaces)
        StringBuilder result = new StringBuilder();
        int column = 0; // Tracks the current column position

        for (char c : builder.toString().toCharArray()) {
            switch (c) {
                case '\t' -> {
                    int spacesToAdd = 4 - (column % 4); // Calculate required spaces
                    if (column >= 16) {
                        spacesToAdd = 0;
                    }
                    result.append(" ".repeat(spacesToAdd));
                    column += spacesToAdd;
                }
                case '\n' -> {
                    result.append(c);
                    column = 0;
                }
                default -> {
                    result.append(c);
                    column++;
                }
            }
        }
        try (PrintWriter writer = new PrintWriter(file.replace(".cm", ".tm"), "UTF-8")) {
            writer.println(result.toString());
        }
    }

    @Override
    public void visit(SimpleDec dec, int level, boolean isAddress) {
        if (dec.global) {
            dec.frameOffset = ProgramStack.globalStackOffset--;
            ProgramStack.globalStack.add(new Node(dec.name, dec, 0));
        } else {
            dec.frameOffset = ProgramStack.frameStackOffset--;
            ProgramStack.frameStack.add(new Node(dec.name, dec, level));
        }
    }

    @Override
    public void visit(ArrayDec dec, int level, boolean isAddress) {
        if (dec.global) {
            dec.frameOffset = ProgramStack.globalStackOffset;
            ProgramStack.globalStackOffset -= dec.size;
            ProgramStack.globalStack.add(new Node(dec.name, dec, 0));
        } else {
            dec.frameOffset = ProgramStack.frameStackOffset;
            ProgramStack.frameStackOffset -= dec.size;
            ProgramStack.frameStack.add(new Node(dec.name, dec, level));
        }
    }

    @Override
    public void visit(FunctionDec dec, int level, boolean isAddress) {
        ProgramStack.frameStackOffset = -2;

        builder.append("* -> Beginning of function ").append(dec.name).append("\n");
        beginSection();
        if (dec.name.equals("main")) {
            mainFunctionAddress = line;
        }

        builder.append("* -> Storing return address in stack frame\n");
        dec.address = line;
        functions.add(dec);
        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, -1, Registers.FramePointer);     // Store the return address

        // TODO: where/how to process parameters? accept each?
        if (dec.params != null) {
            dec.params.accept(this, level + 1, false);
        }

        dec.body.accept(this, level + 1, false);

        builder.append("* <- Return to caller\n");
        MemoryInstruction.print(MemoryInstruction.Load, Registers.ProgramCounter, -1, Registers.FramePointer); // Load the return address, and return to caller.

        endSection();
        builder.append("* <- End of function ").append(dec.name).append("\n");
        ProgramStack.frameStackOffset = 0;
    }

    @Override
    public void visit(IfExp exp, int level, boolean isAddress) {

        builder.append("* -> Test for if statement ").append("\n");
        exp.test.accept(this, level, false);

        if (exp.test instanceof OpExp opExp) {
            switch (opExp.op) {
                // TODO: or? add/subtract/something else?
                // TODO: -, +, *, /?
                case OpExp.AND, OpExp.NE, OpExp.OR, OpExp.MINUS, OpExp.PLUS, OpExp.MULTIPLY, OpExp.DIVIDE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpNotEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter); // Load the constant value into the accumulator.
                }
                case OpExp.EQ -> {
                    MemoryInstruction.print(MemoryInstruction.JumpEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter); // Load the constant value into the accumulator.
                }
                case OpExp.GT -> {
                    MemoryInstruction.print(MemoryInstruction.JumpGreaterThan, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.GE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpGreaterEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.LE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpLessEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.LT -> {
                    MemoryInstruction.print(MemoryInstruction.JumpLessThan, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
            }
        }

        MemoryInstruction.print(MemoryInstruction.LoadConstant); // False case
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.ProgramCounter, 1, Registers.ProgramCounter);
        MemoryInstruction.print(MemoryInstruction.LoadConstant, Registers.AccumulatorA, 1, Registers.AccumulatorA); // True case

        var _line = line++;
        builder.append("* <- Test for if statement ").append("\n");

        exp.body.accept(this, level, false);

        MemoryInstruction.print(_line + 1, MemoryInstruction.JumpEqual, Registers.AccumulatorA, (line - _line), Registers.ProgramCounter);

        if (!(exp._else instanceof NilExp)) {
            _line = line++;
            exp._else.accept(this, level, false);
            MemoryInstruction.print(_line + 1, MemoryInstruction.LoadAddress, Registers.ProgramCounter, (line - _line) - 1, Registers.ProgramCounter);
        }
    }

    @Override
    public void visit(OpExp exp, int level, boolean isAddress) {
        int lhsOffset = ProgramStack.frameStackOffset;
        exp.left.accept(this, level, false);
        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, ProgramStack.frameStackOffset--, Registers.FramePointer);
        exp.right.accept(this, level, false);

        MemoryInstruction.print(MemoryInstruction.Load, Registers.AccumulatorB, lhsOffset, Registers.FramePointer);

        // need to add unary
        switch (exp.op) {
            case OpExp.PLUS -> {
                RegisterInstruction.print(RegisterInstruction.Add, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.MINUS -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.DIVIDE -> {
                RegisterInstruction.print(RegisterInstruction.Divide, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.MULTIPLY -> {
                RegisterInstruction.print(RegisterInstruction.Multiply, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.EQ -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.GE -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.GT -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.LT -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.LE -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.NE -> {
                RegisterInstruction.print(RegisterInstruction.Subtract, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.AND -> {
                RegisterInstruction.print(RegisterInstruction.Multiply, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
            case OpExp.OR -> {
                RegisterInstruction.print(RegisterInstruction.Add, Registers.AccumulatorA, Registers.AccumulatorB, Registers.AccumulatorA);
            }
        }
        ProgramStack.frameStackOffset = lhsOffset;
    }

    @Override
    public void visit(IntExp exp, int level, boolean isAddress) {
        builder.append("* -> Loading constant ").append("\n");
        MemoryInstruction.print(MemoryInstruction.LoadConstant, Registers.AccumulatorA, exp.value, Registers.Default); // Load the constant value into the accumulator.
        builder.append("* <- Loaded constant ").append("\n");
    }

    @Override
    public void visit(NilExp exp, int level, boolean isAddress) {
    }

    @Override
    public void visit(VarExp exp, int level, boolean isAddress) {
        exp._var.accept(this, level, isAddress);
    }

    @Override
    public void visit(BoolExp exp, int level, boolean isAddress) {
        int value = exp.value ? 1 : 0;
        builder.append("* -> Loading constant ").append("\n");
        MemoryInstruction.print(MemoryInstruction.LoadConstant, Registers.AccumulatorA, value, Registers.Default); // Load the constant value into the accumulator.
        builder.append("* <- Loaded constant ").append("\n");
    }

    @Override
    public void visit(WhileExp exp, int level, boolean isAddress) {
        int lineStart = line;

        builder.append("* -> Test for while loop ").append("\n");
        exp.test.accept(this, level, false);
        if (exp.test instanceof OpExp opExp) {
            switch (opExp.op) {
                // TODO: or? add/subtract/something else?
                // TODO: -, +, *, /?
                case OpExp.AND, OpExp.NE, OpExp.OR, OpExp.MINUS, OpExp.PLUS, OpExp.MULTIPLY, OpExp.DIVIDE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpNotEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter); // Load the constant value into the accumulator.
                }
                case OpExp.EQ -> {
                    MemoryInstruction.print(MemoryInstruction.JumpEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter); // Load the constant value into the accumulator.
                }
                case OpExp.GT -> {
                    MemoryInstruction.print(MemoryInstruction.JumpGreaterThan, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.GE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpGreaterEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.LE -> {
                    MemoryInstruction.print(MemoryInstruction.JumpLessEqual, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
                case OpExp.LT -> {
                    MemoryInstruction.print(MemoryInstruction.JumpLessThan, Registers.AccumulatorA, 2, Registers.ProgramCounter);
                }
            }
        }
        MemoryInstruction.print(MemoryInstruction.LoadConstant); // False case
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.ProgramCounter, 1, Registers.ProgramCounter);
        MemoryInstruction.print(MemoryInstruction.LoadConstant, Registers.AccumulatorA, 1, Registers.AccumulatorA); // True case

        var _line = line++;

        builder.append("* <- Test for while loop ").append("\n");

        exp.body.accept(this, level, false);

        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.ProgramCounter, -(line - lineStart) - 1, Registers.ProgramCounter);
        MemoryInstruction.print(_line + 1, MemoryInstruction.JumpEqual, Registers.AccumulatorA, (line - _line) - 1, Registers.ProgramCounter);
    }

    @Override
    public void visit(AssignExp exp, int level, boolean isAddress) {

        builder.append("* -> Assigning ").append(exp.left._var.name).append("\n");
        VarExp left = exp.left;
        Exp right = exp.right;

        left.accept(this, level, true);

        var _frameStackOffset = ProgramStack.frameStackOffset;

        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, ProgramStack.frameStackOffset--, Registers.FramePointer);

        right.accept(this, level, false);

        MemoryInstruction.print(MemoryInstruction.Load, Registers.AccumulatorB, _frameStackOffset, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, 0, Registers.AccumulatorB); // Store the value of the right expression into the left variable.
        ++ProgramStack.frameStackOffset;
        builder.append("* <- Exiting assigning ").append(exp.left._var.name).append("\n");
    }

    @Override
    public void visit(ReturnExp exp, int level, boolean isAddress) {
        exp.exp.accept(this, level, false);
        MemoryInstruction.print(MemoryInstruction.Load, Registers.ProgramCounter, -1, Registers.FramePointer);
    }

    @Override
    public void visit(CompoundExp exp, int level, boolean isAddress) {
        exp.decs.accept(this, level, false);

        for (var item : exp.exps.getFlattened()) {
            item.accept(this, level, false);
        }
    }

    @Override
    public void visit(CallExp exp, int level, boolean isAddress) {
        var originalStackValue = ProgramStack.frameStackOffset;

        ProgramStack.frameStackOffset -= 2;

        if (exp.args != null) {
            for (var arg : exp.args.getFlattened()) {
                arg.accept(this, level, false);
                MemoryInstruction.print(MemoryInstruction.Store, Registers.AccumulatorA, ProgramStack.frameStackOffset--, Registers.FramePointer); // Store the argument value into the stack frame.
            }
        }

        ProgramStack.frameStackOffset = originalStackValue;

        builder.append("* -> call of function: ").append(exp.func).append("\n");

        FunctionDec function = null;
        for (FunctionDec dec : functions) {
            if (dec.name.equals(exp.func)) {
                function = dec;
            }
        }
        originalStackValue = ProgramStack.frameStackOffset;
        MemoryInstruction.print(MemoryInstruction.Store, Registers.FramePointer, ProgramStack.frameStackOffset, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.FramePointer, ProgramStack.frameStackOffset, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.AccumulatorA, 1, Registers.ProgramCounter); // TODO: Need to lookup function for address
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.ProgramCounter, -(line - function.address + 1), Registers.ProgramCounter); // TODO: Need to lookup function for address

        MemoryInstruction.print(MemoryInstruction.Load, Registers.FramePointer, 0, Registers.FramePointer);     // Pop frame pointer
        ProgramStack.frameStackOffset = originalStackValue;
        builder.append("* <- end call of function: ").append(exp.func).append("\n");
    }

    @Override
    public void visit(NameTy type, int level, boolean isAddress) {

    }

    @Override
    public void visit(DecList list, int level, boolean isAddress) {
        // Setup
        FunctionDec input = new FunctionDec(-1, -1, new NameTy(-1, -1, NameTy.INT), "input", null, new NilExp(- 1, - 1));
        input.address = 3;
        functions.add(input);

        var output = new FunctionDec(-1, -1, new NameTy(-1, -1, NameTy.VOID), "output", new VarDecList(new SimpleDec(-1, -1, new NameTy(-1, -1, NameTy.INT), null), null), new NilExp(-1, -1));
        output.address = 7;
        functions.add(output);

        MemoryInstruction.print(MemoryInstruction.Load, Registers.GlobalPointer, 0, Registers.Default);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.FramePointer, 0, Registers.GlobalPointer);
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
            if (item instanceof VarDec variable) {
                variable.global = true;
            }
            item.accept(this, level, false);
        }

        // Exit
        MemoryInstruction.print(MemoryInstruction.Store, Registers.FramePointer, ProgramStack.globalStackOffset, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.FramePointer, ProgramStack.globalStackOffset, Registers.FramePointer);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.AccumulatorA, 1, Registers.ProgramCounter);
        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.ProgramCounter, -(line - mainFunctionAddress + 1), Registers.ProgramCounter);
        MemoryInstruction.print(MemoryInstruction.Load, Registers.FramePointer, 0, Registers.FramePointer);
        RegisterInstruction.print(RegisterInstruction.Halt);
    }

    @Override
    public void visit(ExpList list, int level, boolean isAddress) {
        for (Exp item : list.getFlattened()) {
            item.accept(this, level, false);
        }
    }

    @Override
    public void visit(VarDecList list, int level, boolean isAddress) {
        for (var item : list.getFlattened()) {
            item.accept(this, level, false);
        }
    }

    @Override
    public void visit(IndexVar var, int level, boolean isAddress) {
        // ST 0, offset + index(5/6)

    }

    @Override
    public void visit(SimpleVar _var, int level, boolean isAddress) {
        var node = ProgramStack.find(_var.name);

        int register;
        var dec = (VarDec) node.dec;

        if (dec.global) {
            register = Registers.GlobalPointer;
        } else {
            register = Registers.FramePointer;
        }

        if (!isAddress) {
            MemoryInstruction.print(MemoryInstruction.Load, Registers.AccumulatorA, ((VarDec) node.dec).frameOffset, register); // Load the variable value into the accumulator.
            return;
        }

        MemoryInstruction.print(MemoryInstruction.LoadAddress, Registers.AccumulatorA, ((VarDec) node.dec).frameOffset, register); // Load the variable address into the accumulator.
    }
}
