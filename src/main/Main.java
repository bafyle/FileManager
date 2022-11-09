package main;
import javax.swing.*;
import GUI.*;

public class Main
{
    
    public static void main(String []args)
    {
        JFrame mainFrame = new JFrame("File Manager");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(UI.FIXED_DIMENSION);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        UI mg = new UI(mainFrame);
        mainFrame.add(mg.run());
        mainFrame.setVisible(true);
    }
}