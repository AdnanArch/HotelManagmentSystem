package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class Bookings extends JFrame{
    JTextField searchField;
    RoundedButton searchButton;
    Bookings(){
        super("Booking Management");

//        searchField = new JTextField();
//        searchField.setBounds(60, 50, 250, 40);
//        searchField.setFont(new Font("Arial", Font.PLAIN, 17));
//        add(searchField);

//        searchButton = new RoundedButton("Search");
//        searchButton.setBounds(320, 50, 130, 40);
//        searchButton.setBackground(new Color(136, 208, 219));
//        searchButton.setFont(new Font("Arial", Font.BOLD, 17));
//        searchButton.setFocusable(false);
//        searchButton.addActionListener(this);
//        add(searchButton);

        setBounds(400, 170,1500, 850);
        setBackground(new Color(52, 136, 157));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Bookings::new);
    }
}