package screens;

import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Bookings extends JFrame implements ActionListener {
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
        searchButton.addActionListener(this);
        searchButton.setFocusable(false);
        add(searchButton);

        RoundedButton clearButton = new RoundedButton("Clear");
        clearButton.setBounds(480, 50, 130, 40);
        clearButton.setBackground(new Color(136, 208, 219));
        clearButton.setFont(new Font("Arial", Font.BOLD, 17));
        clearButton.addActionListener(this);
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void fetchAndRefreshBookingsDataFromDatabase() {
        clearTableData(); // Clear the existing table data
        clearTableColumns();
        try {
            // Create an instance of DatabaseConnection to establish the database connection
            DatabaseConnection dbConnection = new DatabaseConnection();

            CallableStatement statement = dbConnection.connection.prepareCall("{call sp_get_booking_details()}");

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Get the metadata of the result set
            ResultSetMetaData metaData = resultSet.getMetaData();

            // Get the number of columns in the result set
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the table model
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            // Close the result set and database connection
            resultSet.close();
            dbConnection.connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearTableColumns() {
        while (tableModel.getColumnCount() > 0) {
            tableModel.setColumnCount(0);
        }
    }

    private void clearTableData() {
        tableModel.setRowCount(0);
    }


    public static void main(String[] args) {
        new Bookings();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == searchButton) {
            String searchTerm = searchField.getText();
            clearTableColumns();
            clearTableData(); // Clear the existing table data

            try {
                // Create an instance of DatabaseConnection to establish the database connection
                DatabaseConnection dbConnection = new DatabaseConnection();

                // Create the SQL query with a WHERE clause to search for matching rooms
                String sqlQuery = """
                        SELECT c.customer_id AS ID, c.first_name, c.last_name, c.phone, r.room_no, rt.room_type, b.booking_status, r.room_status, b.start_date, b.end_date, b.booking_date
                        FROM bookings AS b
                        JOIN customers AS c ON b.customer_id = c.customer_id
                        JOIN rooms AS r ON b.room_no = r.room_no
                        JOIN room_types AS rt ON r.type_id = rt.type_id WHERE r.room_no LIKE ? OR rt.room_type LIKE ?""";

                // Prepare the statement
                PreparedStatement preparedStatement = dbConnection.connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, "%" + searchTerm + "%"); // Use wildcard '%' to match any part of the room number
                preparedStatement.setString(2, "%" + searchTerm + "%"); // Use wildcard '%' to match any part of the room type

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Get the metadata of the result set
                ResultSetMetaData metaData = resultSet.getMetaData();

                // Get the number of columns in the result set
                int columnCount = metaData.getColumnCount();

                // Add column names to the table model
                for (int i = 1; i <= columnCount; i++) {
                    tableModel.addColumn(metaData.getColumnName(i));
                }

                // Add rows to the table model
                while (resultSet.next()) {
                    Object[] rowData = new Object[columnCount];
                    for (int i = 1; i <= columnCount; i++) {
                        rowData[i - 1] = resultSet.getObject(i);
                    }
                    tableModel.addRow(rowData);
                }

                resultSet.close();// Close the result set, statement, and database connection
                preparedStatement.close();
                dbConnection.connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            searchField.setText("");
            fetchAndRefreshBookingsDataFromDatabase();
        }
    }
}
