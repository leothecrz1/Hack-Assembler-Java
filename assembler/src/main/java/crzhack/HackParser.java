package crzhack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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

    private FileReader inputReader;
    private BufferedReader bufferedReader;

    public HackParser(String filepath)
    {
        lineNumber = 0;
        symbolTable = new HashMap<String, Integer>();

        try 
        {
            inputReader = new FileReader( new File(filepath) );
            bufferedReader = new BufferedReader(inputReader);
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        fillSymbolTableDefaults();
        parseSymbols();
    }

    private String trimComments(String str)
    {
        int index = str.indexOf("//");
        if(index == -1)
            return str;

        return str.substring(0, index);
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

    private void resetFields()
    {
        symbol = "";
        comp = "";
        dest = "";
        jump = "";
        type = InstructionTypes.UNSET;
    }

    private void AINS()
    {
        activeLine = trimComments(activeLine);
        activeLine.trim();
        type = InstructionTypes.A_INSTRUCTION;
        symbol = activeLine.substring(1);
    }

    private void CINS()
    {
        activeLine = trimComments(activeLine);
        activeLine.trim();
        type = InstructionTypes.C_INSTRUCTION;

        int destBreakPoint = activeLine.indexOf('=');
        int jumpBreakPoint = activeLine.indexOf(';');

        if(destBreakPoint == -1)
        {
            if(jumpBreakPoint == -1)
            {
                comp = activeLine;
                return;
            }

            comp = activeLine.substring(0, jumpBreakPoint);
            jump = activeLine.substring(jumpBreakPoint+1);
            return;
        }

        if(jumpBreakPoint == -1)
        {
            dest = activeLine.substring(0, destBreakPoint);
            comp = activeLine.substring(jumpBreakPoint+1);
            return;
        }

        dest = activeLine.substring(0, destBreakPoint);
        comp = activeLine.substring(destBreakPoint+1, jumpBreakPoint);
        jump = activeLine.substring(jumpBreakPoint+1);

    }

    private void LINS()
    {
        activeLine = trimComments(activeLine);
        activeLine = activeLine.trim();
        type = InstructionTypes.L_INSTRUCTION;
        symbol = activeLine.substring(1, activeLine.length()-2);
    }

    private void setInstruction()
    {
        activeLine = activeLine.trim();
        resetFields();

        if(activeLine.length() < 1)
            return;

        char firstCharacter = activeLine.charAt(0);
        switch (firstCharacter) 
        {
            case '@':
                lineNumber++;
                AINS();
                break;
        
            case '(':
                LINS();
                break;

            case '/':
                break;

            default:
                lineNumber++;
                CINS();
                break;
        }
    }

    private void parseSymbols()
    {
        while(!isAtEndOfFile())
        {
            if(type == InstructionTypes.A_INSTRUCTION)
            {
                try {
                    Integer.parseInt(symbol);
                } catch (NumberFormatException ignore) {
                    if( symbolTable.get(symbol) == null)
                    {
                        symbolTable.put(symbol, lineNumber);
                    }
                }
            }

            if(type != InstructionTypes.C_INSTRUCTION)
                return;

            int pIndex = activeLine.indexOf('(');
            int pIndexTwo = activeLine.indexOf(')');
            String str = activeLine.substring(pIndex+1, pIndexTwo).trim();
            
            if( symbolTable.put(str, lineNumber) == null )
                System.out.println(symbol + " " + lineNumber);
        }

        try 
        {
            inputReader.reset();
            activeLine = "";
            resetFields();
            lineNumber = 0;
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

    }

    public boolean isAtEndOfFile()
    {
        try 
        {
            if( (activeLine = bufferedReader.readLine()) != null)
            {
                setInstruction();
                return false;
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return true;
    }

    public int getLineNumber() 
    {
        return lineNumber;
    }
    public String getComp() 
    {
        return comp;
    }
    public String getDest() 
    {
        return dest;
    }
    public String getJump() 
    {
        return jump;
    }
    public String getSymbol() 
    {
        return symbol;
    }
    public InstructionTypes getType()
    {
        return type;
    }
    

}
