package fileManager;

public enum FileType
{
    File("File"),
    Directory("Directory"),
    Link("Link File"),
    Unknown("Unknown");

    private String action;
  
    public String getAction()
    {
        return this.action;
    }
    private FileType(String action)
    {
        this.action = action;
    }
}
