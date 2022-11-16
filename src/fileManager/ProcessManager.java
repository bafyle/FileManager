
package fileManager;

import java.io.*;


public class ProcessManager 
{
    public static final String NEW_LINE_TERMINATOR = System.getProperty("line.separator");

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
                res.append(line).append(NEW_LINE_TERMINATOR);
            }
        }
        return res.toString();
    }
    
}
