package screens;

import javax.swing.*;
import java.awt.*;

public class AdminDashBoard extends JFrame {
    AdminDashBoard() {
        super("Admin Dashboard");


        getContentPane().setBackground(new Color(12, 55, 64));
        setLayout(null);
        setResizable(false);
        setBounds(450, 100, 1080, 820);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
