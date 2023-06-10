package screens;

import components.RoundedButton;
import components.RoundedTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class Bookings extends JFrame {
    private static RoundedButton searchButton;
    private static RoundedTextField searchField;
    private static DefaultTableModel tableModel;
    public Bookings(){
        super("Booking Management");

        searchField = new RoundedTextField();
        searchField.setBounds(60, 50, 250, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 17));
        add(searchField);

        searchButton = new RoundedButton("Search");
        searchButton.setBounds(330, 50, 130, 40);
        searchButton.setBackground(new Color(136, 208, 219));
        searchButton.setFont(new Font("Arial", Font.BOLD, 17));
        searchButton.setFocusable(false);
        add(searchButton);

        RoundedButton clearButton = new RoundedButton("Clear");
        clearButton.setBounds(480, 50, 130, 40);
        clearButton.setBackground(new Color(136, 208, 219));
        clearButton.setFont(new Font("Arial", Font.BOLD, 17));
        clearButton.setFocusable(false);
        add(clearButton);

        // Create a table model to hold the data
        tableModel = new DefaultTableModel();

        // Create the table with the table model
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                if (component instanceof JComponent jComponent) {
                    jComponent.setFont(new Font("Arial", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(25); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        // Set table properties
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 110, 1380, 680);
        add(scrollPane);

        // Fetch data from the database and populate the table
        fetchAndRefreshBookingsDataFromDatabase();

        setLayout(null);
        setBounds(400, 170, 1500, 850);
        getContentPane().setBackground(new Color(58, 109, 122));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void fetchAndRefreshBookingsDataFromDatabase() {

    }

    public static void main(String[] args) {
        new Bookings();
    }
}
