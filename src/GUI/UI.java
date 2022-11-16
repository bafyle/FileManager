package GUI;
import javax.swing.*;
import javax.swing.table.*;

import fileManager.File;
import fileManager.ProcessController;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UI
{
    public final static Dimension FIXED_DIMENSION = new Dimension(800, 600);
    private JTable filesTable;
    private JFrame mainFrame;
    private ProcessController pc = new ProcessController();
    public UI(JFrame m)
    {
        mainFrame = m;
    }
    private void showError(JPanel mainPanel, String errorMessage)
    {
        JOptionPane.showConfirmDialog(mainPanel, errorMessage, "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
    }
    private DefaultTableModel refreshTable()
    {
        DefaultTableModel tableModel = new DefaultTableModel();
        pc.refreshFilesList(true);
        tableModel.addColumn("Name");
        tableModel.addColumn("Permissions");
        tableModel.addColumn("Type");
        tableModel.addColumn("Hidden");
        for(File file : pc.getFiles()) 
        {
            String type = file.getType().getAction();
            String hidden = "No";
            if(file.isHidden())
                hidden = "Yes";
            String []row = {file.getName(), file.getPermissions(), type, hidden};
            tableModel.addRow(row);
        }
        return tableModel;
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

        // working directory
        String place = null;
        try
        {
            place = "Current directory: ";
            place += pc.getCWD();
        }
        catch(IOException err){err.printStackTrace();}
        JLabel currentPlace = new JLabel(place);
        currentPlace.setBounds(10, 300, 800 , 30);
        mainPanel.add(currentPlace);
        
        // Buttons
        int x = 170;
        JButton createFileButton = new JButton("Create new file");
        createFileButton.setBounds(10 +x, 350, 200, 25);
        createFileButton.addActionListener((ActionEvent e)->
        {
            String fileName = JOptionPane.showInputDialog(mainPanel, "Enter the file name");
            if(fileName != null)
            {
                if(!pc.isExist(fileName))
                {
                    try{
                    pc.createFile(fileName);
                    filesTable.setModel(refreshTable());
                    }
                    catch(IOException error){error.printStackTrace();}
                }
                else
                    showError(mainPanel, "File already exists");
                
            }
        });
        mainPanel.add(createFileButton);
        
        JButton deleteFileButton = new JButton("Delete file");
        deleteFileButton.setBounds(220+x, 350, 200, 25);
        deleteFileButton.addActionListener((ActionEvent e)->
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
                        try{pc.deleteFile(fileName);}catch(IOException error){Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, error);}
                        filesTable.setModel(refreshTable());
                    }
                }
            }
        });
        mainPanel.add(deleteFileButton);

        JButton createDirectoryButton = new JButton("Create new directory");
        createDirectoryButton.setBounds(10+x, 400, 200, 25);
        createDirectoryButton.addActionListener((ActionEvent e)->
        {
            String dirName = JOptionPane.showInputDialog(mainPanel, "Enter the file name");
            if(dirName != null)
            {
                if(!pc.isExist(dirName))
                {
                    try{pc.createDirectory(dirName);}catch(IOException error){}
                    filesTable.setModel(refreshTable());
                }
                else
                    JOptionPane.showConfirmDialog(mainPanel, "Directory already exist", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        });
        mainPanel.add(createDirectoryButton);
        
        JButton deleteDirectoryButton = new JButton("Delete directory");
        deleteDirectoryButton.setBounds(220+x, 400, 200, 25);
        deleteDirectoryButton.addActionListener((ActionEvent e)->
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
                        try{pc.deleteDirectory(fileName);}catch(IOException error){Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, error);}
                        filesTable.setModel(refreshTable());
                    }
                }
            }
        });
        mainPanel.add(deleteDirectoryButton);
        
        JButton createLinkButton = new JButton("Create a link file");
        createLinkButton.setBounds(10+x, 450, 200, 25);
        createLinkButton.addActionListener((ActionEvent e)->
        {
            String sourceName = JOptionPane.showInputDialog(mainPanel, "Enter the source file name");
            if(sourceName != null)
            {
                String linkName = JOptionPane.showInputDialog(mainPanel, "Enter the link file name");
                if(linkName != null)
                {
                    if(pc.isExist(linkName))
                    {
                        showError(mainPanel, linkName + " is already exists.");
                    }
                    else
                    {
                        try
                        {
                            pc.createLink(linkName, sourceName);
                            filesTable.setModel(refreshTable());
                        }
                        catch(IOException error)
                        {
                            error.printStackTrace();
                        }
                    }
                } 
            }
        });
        mainPanel.add(createLinkButton);
        
        JButton changePermissionsButton = new JButton("Change Permission");
        changePermissionsButton.setBounds(220+x, 450, 200, 25);
        changePermissionsButton.addActionListener((ActionEvent e)->
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
                
                JComboBox<String> box1 = new JComboBox<>(data);
                box1.setBounds(200, 10, 150, 30);
                JComboBox<String> box2 = new JComboBox<>(data);
                box2.setBounds(200, 60, 150, 30);
                JComboBox<String> box3 = new JComboBox<>(data);
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
                        pc.changePermission(fileName, perm);
                        filesTable.setModel(refreshTable());
                    }
                    catch(IOException error)
                    {
                        error.printStackTrace();
                    }
                    innerFrame.dispose();
                });
            }
        });
        mainPanel.add(changePermissionsButton);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBounds(690, 540, 100, 25);
        exitBtn.addActionListener((ActionEvent e)->
        {
            mainFrame.setVisible(false);
            mainFrame.dispose();
            System.exit(0);
        });
        mainPanel.add(exitBtn);
        return mainPanel;
    }
}
