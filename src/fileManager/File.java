
package fileManager;

public class File
{
    private String name;
    private String permissions;
    private FileType type;
    private boolean hidden;
    public File() {
        name = "";
        permissions = "";
        type = FileType.Unknown;
        hidden = false;
    }

    public void setName(String name)
    {
        this.name = name;
        if(this.name.charAt(0) == '.')
            this.hidden = true;
    }
    public String getName(){return this.name;}
    public void setPermissions(String permissions){
        this.permissions = permissions;
        switch(permissions.charAt(0))
        {
            case 'l':
                type = FileType.Link;
                break;
            case 'd':
                type = FileType.Directory;
                break;
            case '-':
                type = FileType.File;
        }
    }
    public String getPermissions(){return this.permissions;}
    public FileType getType(){return this.type;}
    public boolean isHidden(){return this.hidden;}
}
