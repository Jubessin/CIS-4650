# C- Compiler

This is the README for the C- (CMinus) compiler. 

## Build

The compiler source code can be built with the command:

`make`

## Usage

After building, the compiler can be used with the following command:

`java -cp /usr/share/java/cup.jar:. CM <file.cm>`

## Tests

The compiler is bundled with 5 test files stored under `./tests`.

The tests can be used as mentioned in the previous section. For example, with `2.cm`:

`java -cp /usr/share/java/cup.jar:. CM 2.cm`

## References

The example Checkpoint 1 package provided by Professor Fei Song was used as a reference in building this compiler.
