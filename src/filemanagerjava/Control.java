
package filemanagerjava;
import java.io.*;
public class Control
{
    public static void create(String fileName, boolean file) throws IOException
    {
        if(file)
        {
            Foundation.run("touch", fileName);
        }
        else
            Foundation.run("mkdir", fileName);
        Foundation.getFiles(true);
    }
    public static void delete(String fileName, boolean file) throws IOException
    {
        if(file)
        {
            Foundation.run("rm", "-f", fileName);
        }
        else
            Foundation.run("rmdir", fileName);
        Foundation.getFiles(true);
    }
    public static void createLink(String fileName, String source) throws IOException
    {
        Foundation.run("ln", "-s", source, fileName);
        Foundation.getFiles(true);
    }
    
    public static void changePermission(String fileName, String permission) throws IOException
    {
        Foundation.run("chmod", permission, fileName);
        Foundation.getFiles(true);
    }
    
    public static String getCurrentPlace() throws IOException
    {
        StringBuilder output = new StringBuilder(Foundation.run("pwd"));
        output.delete(output.length()-4, output.length());
        return output.toString();
    }
}
