package fileManager;

import java.io.*;
import java.util.ArrayList;

public class ProcessController
{
    private ArrayList<File>Files = new ArrayList<>();

    public ArrayList<File> getFiles()
    {
        return Files;
    }
    public void createFile(String fileName) throws IOException
    {
        ProcessManager.run("touch", fileName);
        refreshFilesList(true);
    }
    public void createDirectory(String dirName) throws IOException
    {
        ProcessManager.run("mkdir", dirName);
        refreshFilesList(true);
    }
    
    public void deleteFile(String fileName) throws IOException
    {
        ProcessManager.run("rm", "-f", fileName);
        refreshFilesList(true);
    }
    public void deleteDirectory(String dirName) throws IOException
    {
        ProcessManager.run("rmdir", dirName);
        refreshFilesList(true);
    }
    
    public void createLink(String fileName, String source) throws IOException
    {
        ProcessManager.run("ln", "-s", source, fileName);
        refreshFilesList(true);
    }
    
    public void changePermission(String fileName, String permission) throws IOException
    {
        ProcessManager.run("chmod", permission, fileName);
        refreshFilesList(true);
    }
    
    public String getCWD() throws IOException
    {
        StringBuilder output = new StringBuilder(ProcessManager.run("pwd"));
        return output.toString().split(ProcessManager.NEW_LINE_TERMINATOR)[0];
    }
    public void refreshFilesList(boolean hidden)
    {
        Files.clear();
        try
        {
            String FilesDetails;
            if(hidden)
                FilesDetails = ProcessManager.run("ls", "-a", "-l");
            else
                FilesDetails = ProcessManager.run("ls", "-l");
            String []splittedFilesDetails = FilesDetails.split(ProcessManager.NEW_LINE_TERMINATOR);
            for(int i = 1; i < splittedFilesDetails.length; i++)
            {
                String fileData[] = splittedFilesDetails[i].split(" +");
                File newFile = new File();
                newFile.setPermissions(fileData[0]);
                newFile.setName(fileData[8]);
                Files.add(newFile);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public boolean isExist(String name)
    {
        if(Files.stream().anyMatch((s) -> (name.equals(s.getName()))))
            return true;
        return false;
    }
}
