package filemanagerjava;
import javax.swing.*;
import GUI.*;

public class Main
{
    
    public static void main(String []args)
    {
        
        JFrame mainFrame = new JFrame("File Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MainGUI.FIXED_DIMENSION);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        MainGUI mg = new MainGUI(mainFrame);
        mainFrame.add(mg.run());
        mainFrame.setVisible(true);
    }
}