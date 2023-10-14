package crzhack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Assembler 
{
    public static void main( String[] args )
    {
        args = new String[1];
        args[0] = "/Users/Repo/Java/Hack-Assembler-Java/test.txt";
        if(args.length != 1)
        {
            System.out.println("Invalid Argument Count");
            System.exit(0);
        }

        File file = new File(args[0]);
        if(!file.exists())
        {
            System.out.println("Input file NOT found.");
            System.exit(1);
        }
        
        file = new File(args[0].substring(0,  args[0].indexOf('.')+1).concat("hack") );
        if(file.exists())
        {
            System.out.print(file.getName());
            System.out.println(" already exists. Replace It? y/n (0/1)");
            
            Scanner input = new Scanner(System.in);
            char in = input.nextLine().charAt(0);
            input.close();
            if(in != 'y' && in != 'Y' && in != 1)
               System.exit(0); 
            
            file.delete();
        }
        FileWriter fileWriter = null;
        try 
        {
            file.createNewFile();
            fileWriter = new FileWriter(file);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        
        HackCoder coder = new HackCoder();
        HackParser parser = new HackParser(args[0]);

        while(!parser.isAtEndOfFile())
        {
            StringBuilder sb = new StringBuilder();
            System.out.println(parser.getInfo());

            switch ( parser.getType() ) 
            {
                case A_INSTRUCTION:
                    {
                        sb.append("0");
                        try 
                        {
                            int symVal = Integer.parseInt( parser.getSymbol() ); 
                            sb.append( String.format("%15s", Integer.toBinaryString(symVal)).replaceAll(" ", "0") );
                            sb.append("\n");
                            try { fileWriter.write(sb.toString()); } catch (IOException e) { e.printStackTrace(); }
                        } 
                        catch (NumberFormatException e) 
                        {
                            if(parser.hasSymbol( parser.getSymbol() ))
                            {
                                
                                sb.append( String.format("%15s", Integer.toBinaryString( parser.getSymVal( parser.getSymbol()) )).replaceAll(" ", "0") );
                                sb.append("\n");
                                try { fileWriter.write(sb.toString()); } catch (IOException ex) { ex.printStackTrace(); }
                            }
                            else
                            {
                                System.out.print("Symbol Could Not Be Found. Sym=");
                                System.out.print( parser.getSymbol() );
                                System.out.println("");
                                try { fileWriter.close(); } catch (IOException e1) { e1.printStackTrace(); }
                                System.exit(1);
                            }
                        }
                    }
                    break;

                case C_INSTRUCTION:
                    sb.append("111");
                    sb.append( coder.compToBin( parser.getComp() ) );
                    sb.append( coder.destToBin( parser.getDest() ) );
                    sb.append( coder.jumpToBin( parser.getJump() ) );
                    sb.append( "\n " );

                    try 
                    {
                        fileWriter.write(sb.toString());
                    } 
                    catch (IOException e) 
                    {
                        e.printStackTrace();
                    }
                    break;
            
                default:
                    break;
            }

        }

    }
}
