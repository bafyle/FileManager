
package filemanagerjava;

import java.io.*;
import java.util.ArrayList;

public class Foundation 
{
    private static final String NEWLINE = System.getProperty("line.seperator");
    public static ArrayList<File>Files = new ArrayList<>();
    
    public static String run(String... command) throws IOException
    {
        ProcessBuilder pb = new ProcessBuilder(command).redirectErrorStream(true);
        Process p = pb.start();
        StringBuilder res = new StringBuilder(80);
        try(BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream())))
        {
            while(true)
            {
                String line = in.readLine();
                if(line== null)
                {
                    break;
                }
                res.append(line).append(NEWLINE);
            }
        }
        return res.toString();
    }
    public static void getFiles(boolean hidden)
    {
        Files.clear();
        try
        {
            String output;
            if(hidden)
                output = run("ls", "-a", "-l");
            else
                output = run("ls", "-l");
            String newOutput[] = output.split("null");
            for(int i = 1; i < newOutput.length; i++)
            {
                String ff[] = newOutput[i].split(" ");
                File newFile = new File();
                newFile.permissions = ff[0];
                switch(ff[0].charAt(0))
                {
                    case 'l':
                        newFile.type = 2;
                        break;
                    case 'd':
                        newFile.type = 1;
                        break;
                    case '-':
                        newFile.type = 0;
                }
                Files.add(newFile);
            }
            String output2;
            if(hidden)
                output2 = run("ls", "-a");
            else
                output2 = run("ls");
            String []newOutput2 = output2.split("null");
            for(int f = 0; f < newOutput2.length; f++)
            {
                Files.get(f).name = newOutput2[f];
            }
        }
        catch(IOException e)
        {
            
        }
    }
    public static boolean isExist(String name)
    {
        if(Files.stream().anyMatch((s) -> (name.equals(s.name)))) {
            return true;
        }
        return false;
    }
   
}
