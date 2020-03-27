package GUI;
import filemanagerjava.Foundation;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import filemanagerjava.File;
import java.io.*;

public class MainGUI
{
    public final static Dimension FIXED_DIMENSION = new Dimension(800, 600);
    private DefaultTableModel refreshTable()
    {
        DefaultTableModel returningModel = new DefaultTableModel();
        Foundation.getFiles(true);
        returningModel.addColumn("Name");
        returningModel.addColumn("Permissions");
        returningModel.addColumn("Type");
        for(File s : Foundation.Files)
        {
            String type = null;
            switch(s.type)
            {
                case 0:
                    type = "File";
                    break;
                case 1:
                    type = "Directory";
                    break;
                case 2:
                    type = "Link File";
                    break;
                default:
                    type = "Unknown";
            }
            String []row = {s.name, s.permissions, type};
            returningModel.addRow(row);
        }
        return returningModel;
    }
    public JPanel run()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(FIXED_DIMENSION);
        
        JTable filesTable = new JTable(refreshTable());
        filesTable.setBounds(10, 10, 780, 300);
        java.io.File fontFile = new java.io.File("fonts/consolab.ttf");
        Font f = null;
        try
        {
            f = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(f);
        }
        catch(FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
        filesTable.setFont(f);
        mainPanel.add(filesTable);
        
        JScrollPane sp = new JScrollPane(filesTable);
        sp.setBounds(10, 10, 780, 300);
        mainPanel.add(sp);
        
        
        return mainPanel;
    }
}
