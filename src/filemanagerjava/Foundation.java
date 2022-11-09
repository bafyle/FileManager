
package filemanagerjava;

import java.io.*;
import java.util.ArrayList;

public class Foundation 
{
    private static final String NEWLINE = System.getProperty("line.separator");
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
            String []splittedOutput = output.split(NEWLINE);
            for(int i = 1; i < splittedOutput.length; i++)
            {
                String fileData[] = splittedOutput[i].split(" ");
                File newFile = new File();
                newFile.permissions = fileData[0];
                switch(fileData[0].charAt(0))
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
            String []splittedOutput2 = output2.split(NEWLINE);
            for(int f = 0; f < splittedOutput2.length; f++)
            {
                Files.get(f).name = splittedOutput2[f];
                if(splittedOutput2[f].charAt(0) == '.')
                    Files.get(f).hidden = true;
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public static boolean isExist(String name)
    {
        if(Files.stream().anyMatch((s) -> (name.equals(s.name))))
            return true;
        return false;
    }
}
