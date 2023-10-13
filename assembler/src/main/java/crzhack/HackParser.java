package crzhack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HackParser 
{

    private Map<String, Integer> symbolTable;

    private String activeLine;
    private String symbol;
    private String comp;
    private String dest;
    private String jump;

    private InstructionTypes type;

    private int lineNumber;

    private File inputFile;

    public HackParser(String filepath)
    {
        lineNumber = 0;
        symbolTable = new HashMap<String, Integer>();

        inputFile = new File(filepath);
        fillSymbolTableDefaults();

        parseSymbols();

    }

    private void fillSymbolTableDefaults()
    {
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }   

    private void parseSymbols()
    {
        try(FileReader tesFileReader = new FileReader(inputFile))
        {
            BufferedReader reader = new BufferedReader(tesFileReader);

            String line;
            while( (line = reader.readLine() ) != null)
            {
                line = line.trim();
                char c = line.charAt(0);
                
                switch (c) 
                {
                    case '(':
                        {
                            int index = line.indexOf(')');
                            
                            break;
                        }
                    case '@':
                        {
                            break;
                        }
                    default:
                        continue;
                }
            }
            reader.close();
        }
        catch (IOException e) 
        {
            
        }
        
    }

}
