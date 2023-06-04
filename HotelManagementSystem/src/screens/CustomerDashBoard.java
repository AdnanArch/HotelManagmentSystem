package screens;

import javax.swing.*;
import java.awt.*;

public class CustomerDashBoard extends JFrame {
    CustomerDashBoard(){
        super("Customer Dashboard");

        setLayout(null);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1080, 820));
        getContentPane().setBackground(new Color(12, 55, 64));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }
}
