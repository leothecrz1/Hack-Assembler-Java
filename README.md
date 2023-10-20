# CRZHACK Assembler

This is a simple Java program that assembles a Hack Assembly Language program into machine code for the Hack computer architecture.

## Usage

To use this program, follow these steps:

1. Ensure you have Java installed on your system.

2. Build the Assembler file using maven:

```
    mvn package
```
#### Manual Compile
For every *.java file run

    javac file.java
    
3. Enter the target folder
```
    cd target
```

4. Execute the compiled program with the path to your Hack Assembly Language file as the argument:

```
   java -jar HackAssembler-0.jar path/to/your/assembly/file.asm
```
   Make sure to replace `path/to/your/assembly/file.asm` with the actual path to your assembly file.

#### Manual Run
With all class files in the same folder run:
```
    java -cp ./.. crzhack.Assembler path/to/your/assembly/file.asm
```

## Command Line Arguments

The program expects one command line argument, which is the path to the Hack Assembly Language file you want to assemble.

## Error Handling

The program includes no IO Exception handling. Will point out what line of input file caused error.

## About the Code

The main logic of the program is in the `main` method of the `Assembler` class. It performs the following steps:

1. Validates command line arguments.
2. Checks if the input file exists.
3. Prompts the user if the output file already exists.
4. Creates a new output file.
5. Initializes a `HackCoder` and a `HackParser`.
6. Processes each line of the input file and generates machine code instructions.
7. Writes the generated machine code to the output file.


