package filemanagerjava;
import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import GUI.*;

public class Main
{
    
    public static void main(String []args)
    {
        MainGUI mg = new MainGUI();
        
        JFrame mainFrame = new JFrame("File Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(MainGUI.FIXED_DIMENSION);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        
        
        
        
        
        
        
        mainFrame.add(mg.run());
        mainFrame.setVisible(true);
    }
}