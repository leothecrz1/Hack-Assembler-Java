package crzhack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;

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

    private File input;
    private FileReader inputReader;
    private BufferedReader bufferedReader;

    public HackParser(String filepath)
    {
        lineNumber = 0;
        symbolTable = new HashMap<String, Integer>();

        try 
        {
            input = new File(filepath);
            inputReader = new FileReader( input );
            bufferedReader = new BufferedReader(inputReader);
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }
        fillSymbolTableDefaults();
        parseSymbols();
    }
    /**
     * Trim comments utility function
     * @param str
     * @return str with no comments
     */
    private String trimComments(String str)
    {
        int index = str.indexOf("//");
        if(index == -1)
            return str;

        return str.substring(0, index);
    }
    /**
     * Prefill table with default syms
     */
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
    /**
     * Reset fields and Instruction Type is set to UNSET
     */
    private void resetFields()
    {
        symbol = "";
        comp = "";
        dest = "";
        jump = "";
        type = InstructionTypes.UNSET;
    }
    /**
     * Handles A instruction
     */
    private void AINS()
    {
        activeLine = trimComments(activeLine);
        activeLine.trim();
        type = InstructionTypes.A_INSTRUCTION;
        symbol = activeLine.substring(1);
    }
    /**
     * Handles C instruction
     */
    private void CINS()
    {
        activeLine = trimComments(activeLine);
        activeLine = activeLine.trim();
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
            comp = activeLine.substring(destBreakPoint+1);
            return;
        }

        dest = activeLine.substring(0, destBreakPoint);
        comp = activeLine.substring(destBreakPoint+1, jumpBreakPoint);
        jump = activeLine.substring(jumpBreakPoint+1);

    }
    /**
     * Handles L instruction
     */
    private void LINS()
    {
        activeLine = trimComments(activeLine);
        activeLine = activeLine.trim();
        type = InstructionTypes.L_INSTRUCTION;
        symbol = activeLine.substring(1, activeLine.length()-1);
    }
    /**
     * Set the instruction type based on input
     */
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
    /**
     * Scan file for all symbols
     */
    private void parseSymbols()
    {
        int variableRamPos = 16;
        LinkedList<String> aSymbols = new LinkedList<>();

        while(!isAtEndOfFile())
        {
            if(type == InstructionTypes.A_INSTRUCTION)
                try  { Integer.parseInt(symbol); }  //Check If Symbol Is NUM
                catch (NumberFormatException ignore)  
                { aSymbols.add(new String(symbol)); }
            

            if(type != InstructionTypes.L_INSTRUCTION)
                continue;

            int pIndex = activeLine.indexOf('(');
            int pIndexTwo = activeLine.indexOf(')');
            String str = activeLine.substring(pIndex+1, pIndexTwo).trim();
            
            if( !symbolTable.containsKey(str) )
            {
                symbolTable.put(str, lineNumber);
                System.out.println(str + " " + lineNumber);
            }
            
        }

        String str;
        while( !aSymbols.isEmpty() ) // Empty A_INS SYMBOLS
        {
            str = aSymbols.pollFirst();
            if( symbolTable.containsKey(str) )
                continue;
            symbolTable.put(str, variableRamPos++);
            System.out.println(str + " " + variableRamPos);  
        }
        //RESET To Begining of file
        try 
        {
            bufferedReader.close();
            inputReader.close();
            inputReader = new FileReader( input  );
            bufferedReader = new BufferedReader(inputReader);
            activeLine = "";
            resetFields();
            lineNumber = 0;
        } 
        catch (IOException e) {e.printStackTrace();}

    }
    /**
     * Advances to next line and check if parser is at the end of the file
     * @return
     */
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
    /**
     * Check if Symbol str exist in the symbolTable
     * @param str
     * @return
     */
    public boolean hasSymbol(String str)
    {
        if(symbolTable.get(str) == null)
            return false;
        return true;
    }
    //GETTERS
    public int getSymVal(String sym)
    {
        return symbolTable.get(sym);
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
    public String getInfo()
    {
        StringBuilder str = new StringBuilder();

        str.append(getLineNumber());
        str.append(" ");

        str.append(getType());
        str.append(" ");

        str.append(getSymbol());
        str.append(" ");

        str.append(getDest());
        str.append(" ");

        str.append(getComp());
        str.append(" ");

        str.append(getJump());
        str.append(" ");

        return str.toString();
    }

}
