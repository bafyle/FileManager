package GUI;
import filemanagerjava.Control;
import filemanagerjava.Foundation;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import filemanagerjava.File;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        delFileBtn.addActionListener((ActionEvent e)->
        {
            if(filesTable.getSelectedRow() == -1)
            {
                JOptionPane.showConfirmDialog(mainPanel, "Please selecte a file from the table", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                String perm = filesTable.getValueAt(filesTable.getSelectedRow(), 1).toString();
                if(perm.charAt(0) == 'd')
                    showError(mainPanel, "Please select a file from the table.");
                else
                {
                    String fileName = filesTable.getValueAt(filesTable.getSelectedRow(), 0).toString();
                    int option = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to delete " + fileName, "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if(option == 0)
                    {
                        try{Control.delete(fileName, true);}catch(IOException error){Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, error);}
                        filesTable.setModel(refreshTable());
                    }
                }
            }
        });
        mainPanel.add(delFileBtn);

        JButton addDirBtn = new JButton("Create new directory");
        addDirBtn.setBounds(10+x, 400, 200, 25);
        addDirBtn.addActionListener((ActionEvent e)->
        {
            String fileName = JOptionPane.showInputDialog(mainPanel, "Enter the file name");
            if(fileName != null)
            {
                if(!Foundation.isExist(fileName))
                {
                    try{Control.create(fileName, false);}catch(IOException error){}
                    filesTable.setModel(refreshTable());
                }
                else
                    JOptionPane.showConfirmDialog(mainPanel, "Directory already exist", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(addDirBtn);
        
        JButton delDirBtn = new JButton("Delete directory");
        delDirBtn.setBounds(220+x, 400, 200, 25);
        delDirBtn.addActionListener((ActionEvent e)->
        {
            if(filesTable.getSelectedRow() == -1)
            {
                JOptionPane.showConfirmDialog(mainPanel, "Please selecte a directory from the table", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                String perm = filesTable.getValueAt(filesTable.getSelectedRow(), 1).toString();
                if(perm.charAt(0) != 'd')
                    showError(mainPanel, "Please select a directory from the table.");
                else
                {
                    String fileName = filesTable.getValueAt(filesTable.getSelectedRow(), 0).toString();
                    int option = JOptionPane.showConfirmDialog(mainPanel, "Are you sure you want to delete " + fileName, "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if(option == 0)
                    {
                        try{Control.delete(fileName, false);}catch(IOException error){Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, null, error);}
                        filesTable.setModel(refreshTable());
                    }
                }
            }
        });
        mainPanel.add(delDirBtn);
        
        JButton addlnkeBtn = new JButton("Create a link file");
        addlnkeBtn.setBounds(10+x, 450, 200, 25);
        addLnkBtn.addActionListener((ActionEvent e)->
        {
            String sourceName = JOptionPane.showInputDialog(mainPanel, "Enter the source file name");
            if(sourceName != null)
            {
                String linkName = JOptionPane.showInputDialog(mainPanel, "Enter the link file name");
                if(linkName != null)
                {
                    if(Foundation.isExist(linkName))
                    {
                        showError(mainPanel, linkName + " is already exists.");
                    }
                    else
                    {
                        try
                        {
                            Control.createLink(linkName, sourceName);
                            filesTable.setModel(refreshTable());
                        }
                        catch(IOException error)
                        {
                        }
                    }
                } 
            }
        });
        mainPanel.add(addlnkeBtn);
        
        JButton chngPerm = new JButton("Change Permission");
        chngPerm.setBounds(220+x, 450, 200, 25);
        chngPerm.addActionListener((ActionEvent e)->
        {
            if(filesTable.getSelectedRow() == -1)
            {
                JOptionPane.showConfirmDialog(mainPanel, "Please select a row from the table", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                String fileName = filesTable.getValueAt(filesTable.getSelectedRow(), 0).toString();
                JFrame innerFrame = new JFrame("Change permissions for " + fileName);
                innerFrame.setSize(450, 230);
                innerFrame.setLayout(null);
                innerFrame.setResizable(false);
                innerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                
                JLabel l1 = new JLabel("Permissions for user:   ");
                l1.setBounds(10, 10, 200, 30);
                innerFrame.add(l1);
                
                JLabel l2 = new JLabel("Permissions for groups: ");
                l2.setBounds(10, 60, 200, 30);
                innerFrame.add(l2);
                
                JLabel l3 = new JLabel("Permissions for others: ");
                l3.setBounds(10, 110, 200, 30);
                innerFrame.add(l3);
                String []data = {"None", "Read-only", "Read-Write", "Read-Write-Execute" };
                
                JComboBox box1 = new JComboBox(data);
                box1.setBounds(200, 10, 150, 30);
                JComboBox box2 = new JComboBox(data);
                box2.setBounds(200, 60, 150, 30);
                JComboBox box3 = new JComboBox(data);
                box3.setBounds(200, 110, 150, 30);
                innerFrame.add(box1);
                innerFrame.add(box2);
                innerFrame.add(box3);
                innerFrame.setVisible(true);
                
                JButton okBtn = new JButton("OK");
                okBtn.setBounds(150, 160, 80, 30);
                innerFrame.add(okBtn);
                
                okBtn.addActionListener((ActionEvent ee)->
                {
                    String perm = "";
                    switch(box1.getSelectedIndex())
                    {
                        case 0:
                            perm += "0";
                            break;
                        case 1:
                            perm += "4";
                            break;
                        case 2:
                            perm += "6";
                            break;
                        case 3:
                            perm += "7";
                    }
                    switch(box2.getSelectedIndex())
                    {
                        case 0:
                            perm += "0";
                            break;
                        case 1:
                            perm += "4";
                            break;
                        case 2:
                            perm += "6";
                            break;
                        case 3:
                            perm += "7";
                    }
                    switch(box3.getSelectedIndex())
                    {
                        case 0:
                            perm += "0";
                            break;
                        case 1:
                            perm += "4";
                            break;
                        case 2:
                            perm += "6";
                            break;
                        case 3:
                            perm += "7";
                    }
                    try
                    {
                        Control.changePermission(fileName, perm);
                        filesTable.setModel(refreshTable());
                    }
                    catch(IOException error)
                    {
                        
                    }
                    innerFrame.dispose();
                });
            }
        });
        mainPanel.add(chngPerm);
        
        return mainPanel;
    }
}
