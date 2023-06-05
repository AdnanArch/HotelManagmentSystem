package screens;

import javax.swing.*;
import java.awt.*;

public class Bookings extends JFrame {
    Bookings(){
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 170, 1500, 850);
        getContentPane().setBackground(new Color(91, 143, 134));
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
    }


}
