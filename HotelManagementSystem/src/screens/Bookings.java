package screens;

import javax.swing.*;
import java.awt.*;

public class Bookings extends JFrame {
    Bookings(){
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setBackground(new Color(12, 55, 64));
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }
}
