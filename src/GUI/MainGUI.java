package GUI;
import javax.swing.*;
import java.awt.*;

public class MainGUI
{
    public final static Dimension FIXED_DIMENSION = new Dimension(800, 600);
    public JPanel run()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setSize(FIXED_DIMENSION);
        
        
        return mainPanel;
    }
}
