package screens;

import javax.swing.*;
import java.awt.*;

public class Revenue extends JFrame {
    Revenue(){
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 170, 1500, 850);
        getContentPane().setBackground(new Color(12, 55, 64));
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }

    public static void main(String[] args) {
        new Revenue();
    }
}