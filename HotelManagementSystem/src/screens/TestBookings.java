package screens;

import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TestBookings extends JFrame implements ActionListener {
    private static final String BUTTON_COLUMN_NAME = "Action";
    private static RoundedButton searchButton;
    private static RoundedTextField searchField;
    private static DefaultTableModel tableModel;

    public TestBookings() {
        super("Booking Management");
        initializeUI();
        fetchAndRefreshBookingsDataFromDatabase();
    }

    private void initializeUI() {
        searchField = new RoundedTextField();
        searchField.setBounds(60, 50, 250, 40);
        searchField.setFont(new Font("Arial", Font.PLAIN, 17));
        add(searchField);

        searchButton = createRoundedButton("Search", 330);
        searchButton.addActionListener(this);
        add(searchButton);

        RoundedButton clearButton = createRoundedButton("Clear", 480);
        clearButton.addActionListener(this);
        add(clearButton);

        tableModel = new DefaultTableModel();
        JTable table = createTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(60, 110, 1380, 680);
        add(scrollPane);

        setLayout(null);
        setBounds(400, 170, 1500, 850);
        getContentPane().setBackground(new Color(58, 109, 122));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private RoundedButton createRoundedButton(String text, int x) {
        RoundedButton button = new RoundedButton(text);
        button.setBounds(x, 50, 130, 40);
        button.setBackground(new Color(136, 208, 219));
        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.addActionListener(this);
        button.setFocusable(false);
        return button;
    }

    private JTable createTable() {
        JTable table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == getColumnCount() - 1) {
                    return new ButtonRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };

        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 17));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    private void fetchAndRefreshBookingsDataFromDatabase() {
        DefaultTableModel model = retrieveBookingsDataFromDatabase();
        updateTableModel(model);
    }

    private DefaultTableModel retrieveBookingsDataFromDatabase() {
        DefaultTableModel model = new DefaultTableModel();
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            CallableStatement statement = dbConnection.connection.prepareCall("{call sp_get_booking_details()}");
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }
            model.addColumn(BUTTON_COLUMN_NAME);
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount + 1];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                rowData[columnCount] = createButton();
                model.addRow(rowData);
            }
            resultSet.close();
            dbConnection.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    private void updateTableModel(DefaultTableModel model) {
        clearTableData();
        clearTableColumns();
        for (int i = 0; i < model.getColumnCount(); i++) {
            tableModel.addColumn(model.getColumnName(i));
        }
        for (int row = 0; row < model.getRowCount(); row++) {
            Object[] rowData = new Object[model.getColumnCount()];
            for (int column = 0; column < model.getColumnCount(); column++) {
                rowData[column] = model.getValueAt(row, column);
            }
            tableModel.addRow(rowData);
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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == searchButton) {
            String searchTerm = searchField.getText();
            DefaultTableModel model = retrieveBookingsDataFromDatabase(searchTerm);
            updateTableModel(model);
        } else {
            searchField.setText("");
            fetchAndRefreshBookingsDataFromDatabase();
        }
    }

    private DefaultTableModel retrieveBookingsDataFromDatabase(String searchTerm) {
        DefaultTableModel model = new DefaultTableModel();
        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            String sqlQuery = "SELECT c.customer_id AS ID, c.first_name, c.last_name, c.phone, r.room_no, rt.room_type, b.booking_status, r.room_status, b.start_date, b.end_date, b.booking_date " +
                    "FROM bookings AS b " +
                    "JOIN customers AS c ON b.customer_id = c.customer_id " +
                    "JOIN rooms AS r ON b.room_no = r.room_no " +
                    "JOIN room_types AS rt ON r.type_id = rt.type_id " +
                    "WHERE r.room_no LIKE ? OR rt.room_type LIKE ?";
            PreparedStatement preparedStatement = dbConnection.connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, "%" + searchTerm + "%");
            preparedStatement.setString(2, "%" + searchTerm + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }
            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                model.addRow(rowData);
            }
            resultSet.close();
            preparedStatement.close();
            dbConnection.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    private JButton createButton() {
        JButton button = new JButton("Click");
        button.addActionListener(this);
        return button;
    }

    private static class ButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Component) {
                return (Component) value;
            } else {
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TestBookings::new);
    }
}
