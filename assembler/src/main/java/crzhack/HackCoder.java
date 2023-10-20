package crzhack;

import java.util.HashMap;
import java.util.Map;

public class HackCoder 
{
    private Map<String, String> compTable;

    public HackCoder()
    {

        compTable = new HashMap<String, String>();

        compTable.put("0", "0101010");
        compTable.put("1", "0111111");
        compTable.put("-1","0111010");
        compTable.put("D","0001100");
        compTable.put("A", "0110000");
        compTable.put("!D", "0001101");
        compTable.put("!A", "0110001");
        compTable.put("-D", "0001111");
        compTable.put("-A", "0110011");

        compTable.put("D+1", "0011111");
        compTable.put("A+1", "0110111");
        compTable.put("D-1", "0001110");
        compTable.put("A-1", "0110010");
        compTable.put("D+A", "0000010");
        compTable.put("D-A", "0010011");
        compTable.put("A-D","0000111");
        compTable.put("D&A", "0000000");
        compTable.put("D|A", "0010101");

        compTable.put("M", "1110000");
        compTable.put("!M", "1110001");
        compTable.put("-M", "1110011");
        compTable.put("M+1", "1110111");
        compTable.put("M-1", "1110010");
        compTable.put("D+M", "1000010");
        compTable.put("D-M", "1010011");
        compTable.put("M-D", "1000111");
        compTable.put("D&M", "1000000");
        compTable.put("D|M", "1010101");
    }

    /**
     * Conver str to its compcode in binary.
     * @param str
     * @return
     */
    public String compToBin(String str)
    {
        String result = compTable.get(str);
        if ( result == null )
        {
            System.out.println( "ERROR in comp section of line"); 
            System.exit(1);
        }
            return result; 
    }

    /**
     * Converts destination portion of instruction to binary
     * @param str
     * @return
     */
    public String destToBin(String str)
    {
        char[] out = {'0','0','0'};

        for(int i=0; i<str.length(); i++)
        {
            switch ( str.charAt(i) )
            {
                case 'M':
                out[2] = '1';
                    break;

                case 'D':
                out[1] = '1';
                    break;

                case 'A':
                out[0] = '1';
                    break;
            
                default:
                    System.out.println( "ERROR in dest section of line" ); 
                    System.exit(1);
            }  
        }
        return String.valueOf(out);
    }

    /**
     * Converts jump portion of instruction to binary
     * @param str
     * @return
     */
    public String jumpToBin(String str)
    {
        char[] out = {'0','0','0'};
        for(int i=0; i<str.length(); i++)
        {
            switch (str.charAt(i))
            {
                case 'M':
                    return "111";
                case 'N':
                    return "101";
                case 'G':
                    out[2] = '1';
                    break;
                case 'E':
                    out[1] = '1';
                    break;
                case 'L':
                    out[0] = '1';
                    break;
                case 'T':
                case 'J':
                    break;
                default:
                    System.out.println( "ERROR in jump section of line" ); 
                    System.exit(1);
            }        
        }
        return String.valueOf(out);
    }

}