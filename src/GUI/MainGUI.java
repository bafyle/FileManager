package GUI;
import filemanagerjava.Control;
import filemanagerjava.Foundation;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import filemanagerjava.File;
import java.awt.event.ActionEvent;
import java.io.*;

public class MainGUI
{
    public final static Dimension FIXED_DIMENSION = new Dimension(800, 600);
    public JTable filesTable;
    private void showError(JPanel mainPanel, String errorMessage)
    {
        JOptionPane.showConfirmDialog(mainPanel, errorMessage, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }
    private DefaultTableModel refreshTable()
    {
        DefaultTableModel returningModel = new DefaultTableModel();
        Foundation.getFiles(true);
        returningModel.addColumn("Name");
        returningModel.addColumn("Permissions");
        returningModel.addColumn("Type");
        returningModel.addColumn("Hidden");
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
            String hidden = null;
            if(s.hidden)
                hidden = "Yes";
            else
                hidden = "No";
            String []row = {s.name, s.permissions, type, hidden};
            returningModel.addRow(row);
        }
        return returningModel;
    }
    public JPanel run()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(FIXED_DIMENSION);
        
        // Files Table 
        filesTable = new JTable(refreshTable());
        filesTable.setDefaultEditor(Object.class, null);
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
        
        // Buttons
        int x = 170;
        JButton addFileBtn = new JButton("Create new file");
        addFileBtn.setBounds(10 +x, 350, 200, 25);
        addFileBtn.addActionListener((ActionEvent e)->
        {
            String fileName = JOptionPane.showInputDialog(mainPanel, "Enter the file name");
            if(fileName != null)
            {
                if(!Foundation.isExist(fileName))
                {
                    try{
                    Control.create(fileName, true);
                    filesTable.setModel(refreshTable());
                    }
                    catch(IOException error){}
                }
                else
                    showError(mainPanel, "File already exists");
                
            }
        });
        mainPanel.add(addFileBtn);
        
        JButton delFileBtn = new JButton("Delete file");
        delFileBtn.setBounds(220+x, 350, 200, 25);
        mainPanel.add(delFileBtn);
        
        JButton addDirBtn = new JButton("Create new directory");
        addDirBtn.setBounds(10+x, 400, 200, 25);
        mainPanel.add(addDirBtn);
        
        JButton delDirBtn = new JButton("Delete directory");
        delDirBtn.setBounds(220+x, 400, 200, 25);
        mainPanel.add(delDirBtn);
        
        JButton addlnkeBtn = new JButton("Create a link file");
        addlnkeBtn.setBounds(10+x, 450, 200, 25);
        mainPanel.add(addlnkeBtn);
        
        JButton chngPerm = new JButton("Change Permission");
        chngPerm.setBounds(220+x, 450, 200, 25);
        mainPanel.add(chngPerm);
        
        return mainPanel;
    }
}
