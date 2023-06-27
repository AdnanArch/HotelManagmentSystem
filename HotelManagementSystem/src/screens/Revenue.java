package screens;

import connection.DatabaseConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;


public class Revenue extends JFrame {
    private final JPanel specificRoomPanel;
    private final JPanel roomRevenueOverTimePanel;
    private final JPanel monthlyRevenuePanel;
    private final JPanel totalRevenuePanel;


    public Revenue() {
        setTitle("Revenue Reports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create "Reports" menu
        JMenu reportsMenu = new JMenu("Reports");
        reportsMenu.setFont(new Font("Arial", Font.PLAIN, 20));

        // Create submenu items for different reports
        JMenuItem specificRoomReportItem = new JMenuItem("Specific Room Report");
        specificRoomReportItem.setFont(new Font("Arial", Font.PLAIN, 18));
        JMenuItem roomRevenueOverTimeItem = new JMenuItem("Room Revenue Over Time Report");
        roomRevenueOverTimeItem.setFont(new Font("Arial", Font.PLAIN, 18));
        JMenuItem monthlyRevenueReportItem = new JMenuItem("Monthly Revenue Report");
        monthlyRevenueReportItem.setFont(new Font("Arial", Font.PLAIN, 18));
        JMenuItem totalRevenueReportItem = new JMenuItem("Total Revenue Report");
        totalRevenueReportItem.setFont(new Font("Arial", Font.PLAIN, 18));

        // Add submenu items to "Reports" menu
        reportsMenu.add(specificRoomReportItem);
        reportsMenu.add(roomRevenueOverTimeItem);
        reportsMenu.add(monthlyRevenueReportItem);
        reportsMenu.add(totalRevenueReportItem);

        // Add "Reports" menu to menu bar
        menuBar.add(reportsMenu);

        // Set menu bar to the frame
        setJMenuBar(menuBar);

        // Create panels for different reports
        specificRoomPanel = createSpecificRoomPanel();
        roomRevenueOverTimePanel = createRoomRevenueOverTimePanel();
        monthlyRevenuePanel = createMonthlyRevenuePanel();
        totalRevenuePanel = createTotalRevenuePanel();

        // Event listeners for submenu items
        specificRoomReportItem.addActionListener(e -> showPanel(specificRoomPanel));

        roomRevenueOverTimeItem.addActionListener(e -> showPanel(roomRevenueOverTimePanel));

        monthlyRevenueReportItem.addActionListener(e -> showPanel(monthlyRevenuePanel));

        totalRevenueReportItem.addActionListener(e -> showPanel(totalRevenuePanel));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Revenue::new);
    }

    private JPanel createSpecificRoomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create input fields and button for specific room report
        JTextField roomNumberTextField = new JTextField(10);
        JTextField monthTextField = new JTextField(10);
        JTextField yearTextField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.setFocusable(false);

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Room No");
        tableModel.addColumn("Check In");
        tableModel.addColumn("Check Out");
        tableModel.addColumn("Revenue");

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

        table.setRowHeight(24); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        JButton printButton = new JButton("Print Report");

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Room Number:"));
        inputPanel.add(roomNumberTextField);
        inputPanel.add(new JLabel("Month:"));
        inputPanel.add(monthTextField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearTextField);
        inputPanel.add(submitButton);

        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButton, BorderLayout.SOUTH);

        // Event listener for submit button
        submitButton.addActionListener(e -> {
            int room_No = Integer.parseInt(roomNumberTextField.getText());
            int month = Integer.parseInt(monthTextField.getText());
            int year = Integer.parseInt(yearTextField.getText());

            // Call the stored procedure and retrieve the data
            try {
                DatabaseConnection db = new DatabaseConnection();
                CallableStatement statement = db.connection.prepareCall("{CALL get_specific_room_revenue_report(?, ?, ?, ?)}");

                // Set the input parameters
                statement.setString(1, String.valueOf(room_No));
                statement.setString(2, String.valueOf(month));
                statement.setString(3, String.valueOf(year));
                statement.registerOutParameter(4, Types.BOOLEAN);
                // Execute the stored procedure
                statement.execute();
                boolean dataExists = statement.getBoolean(4);
                if (dataExists) {
                    ResultSet resultSet;
                    // Execute the stored procedure
                    resultSet = statement.executeQuery();

                    // Populate the table with data
                    while (resultSet.next()) {
                        int bookingID = resultSet.getInt("booking_id");
                        int roomNo = resultSet.getInt("room_no");
                        String checkInDate = resultSet.getString("start_date");
                        String checkOutDate = resultSet.getString("end_date");
                        double revenue = resultSet.getDouble("price");

                        // Add the row data to the table model
                        tableModel.addRow(new Object[]{bookingID, roomNo, checkInDate, checkOutDate, revenue});
                    }
                    resultSet.close();

                } else {
                    JOptionPane.showMessageDialog(null, "No data found");
                }
            } catch (SQLException ex) {
                // Handle any database errors
                ex.printStackTrace();
            }
        });


        // Event listener for print button
        return getjPanel(panel, tableModel, printButton);
    }

    private JPanel createRoomRevenueOverTimePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create input fields and button for specific room report
        JTextField roomNumberTextField = new JTextField(10);
        JButton submitButton = new JButton("Submit");
        submitButton.setFocusable(false);

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Check In");
        tableModel.addColumn("Check Out");
        tableModel.addColumn("Revenue");

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

        table.setRowHeight(24); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        JButton printButton = new JButton("Print Report");

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Room Number:"));
        inputPanel.add(roomNumberTextField);
        inputPanel.add(submitButton);

        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButton, BorderLayout.SOUTH);

        // Event listener for submit button
        submitButton.addActionListener(e -> {
            int room_No = Integer.parseInt(roomNumberTextField.getText());

            // Call the stored procedure and retrieve the data
            try {
                DatabaseConnection db = new DatabaseConnection();
                CallableStatement statement = db.connection.prepareCall("{CALL get_room_revenue_over_time_report(?, ?)}");

                // Set the input parameters
                statement.setString(1, String.valueOf(room_No));
                statement.registerOutParameter(2, Types.BOOLEAN);
                // Execute the stored procedure
                statement.execute();
                boolean dataExists = statement.getBoolean(2);
                if (dataExists) {
                    ResultSet resultSet;
                    // Execute the stored procedure
                    resultSet = statement.executeQuery();

                    // Populate the table with data
                    while (resultSet.next()) {
                        int bookingID = resultSet.getInt("booking_id");
                        String checkInDate = resultSet.getString("start_date");
                        String checkOutDate = resultSet.getString("end_date");
                        double revenue = resultSet.getDouble("price");

                        // Add the row data to the table model
                        tableModel.addRow(new Object[]{bookingID, checkInDate, checkOutDate, revenue});
                    }
                    resultSet.close();

                }
//                else {
//                    JOptionPane.showMessageDialog(null, "No data found");
//                }
            } catch (SQLException ex) {
                // Handle any database errors
                ex.printStackTrace();
            }
        });


        // Event listener for print button
        return getjPanel(panel, tableModel, printButton);
    }

    private JPanel createMonthlyRevenuePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create input fields and button for specific room report


        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Room No");
        tableModel.addColumn("Check In");
        tableModel.addColumn("Check Out");
        tableModel.addColumn("Revenue");

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

        table.setRowHeight(24); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        JButton printButton = new JButton("Print Report");

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());


        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButton, BorderLayout.SOUTH);

        // Event listener for submit button


        // Call the stored procedure and retrieve the data
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL get_monthly_revenue_report(?)}");

            // Set the input parameters

            statement.registerOutParameter(1, Types.BOOLEAN);
            // Execute the stored procedure
            statement.execute();
            boolean dataExists = statement.getBoolean(1);
            if (dataExists) {
                ResultSet resultSet;
                // Execute the stored procedure
                resultSet = statement.executeQuery();

                // Populate the table with data
                while (resultSet.next()) {
                    int bookingID = resultSet.getInt("booking_id");
                    int roomNo = resultSet.getInt("room_no");
                    String checkInDate = resultSet.getString("start_date");
                    String checkOutDate = resultSet.getString("end_date");
                    double revenue = resultSet.getDouble("price");

                    // Add the row data to the table model
                    tableModel.addRow(new Object[]{bookingID, roomNo, checkInDate, checkOutDate, revenue});
                }
                resultSet.close();

            }
//            else {
//                JOptionPane.showMessageDialog(null, "No data found");
//            }
        } catch (SQLException ex) {
            // Handle any database errors
            ex.printStackTrace();
        }


        // Event listener for print button
        return getjPanel(panel, tableModel, printButton);
    }

    private JPanel createTotalRevenuePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("Booking ID");
        tableModel.addColumn("Room No");
        tableModel.addColumn("Check In");
        tableModel.addColumn("Check Out");
        tableModel.addColumn("Revenue");

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

        table.setRowHeight(24); // Set the desired row height
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        JButton printButton = new JButton("Print Report");

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());


        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButton, BorderLayout.SOUTH);


        // Call the stored procedure and retrieve the data
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL get_total_revenue_report(?)}");


            statement.registerOutParameter(1, Types.BOOLEAN);
            // Execute the stored procedure
            statement.execute();
            boolean dataExists = statement.getBoolean(1);
            if (dataExists) {
                ResultSet resultSet;
                // Execute the stored procedure
                resultSet = statement.executeQuery();

                // Populate the table with data
                while (resultSet.next()) {
                    int bookingID = resultSet.getInt("booking_id");
                    int roomNo = resultSet.getInt("room_no");
                    String checkInDate = resultSet.getString("start_date");
                    String checkOutDate = resultSet.getString("end_date");
                    double revenue = resultSet.getDouble("price");

                    // Add the row data to the table model
                    tableModel.addRow(new Object[]{bookingID, roomNo, checkInDate, checkOutDate, revenue});
                }
                resultSet.close();

            }
//            else {
//                JOptionPane.showMessageDialog(null, "No data found");
//            }
        } catch (SQLException ex) {
            // Handle any database errors
            ex.printStackTrace();
        }


        // Event listener for print button
        return getjPanel(panel, tableModel, printButton);
    }

    private JPanel getjPanel(JPanel panel, DefaultTableModel tableModel, JButton printButton) {
        printButton.addActionListener(e -> {
            // Generate PDF of the table data
            try {
                PDDocument document = new PDDocument();
                // Create a new page
                PDPage page = new PDPage();
                document.addPage(page);

                PDType0Font font = PDType0Font.load(document, ClassLoader.getSystemResource("fonts/CascadiaMono.ttf").openStream());

                // Create a content stream for the page
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Set font and font size for the table
                contentStream.setFont(font, 12);

                // Set table parameters
                final int rows = tableModel.getRowCount();
                final int cols = tableModel.getColumnCount();
                final int rowHeight = 20;
                final int tableWidth = 500;
                final int colWidth = tableWidth / (cols + 1);
                final int cellMargin = 5;

                // Set table position on the page
                final float startX = (page.getMediaBox().getWidth() - tableWidth) / 2;

                // Draw table headers
                float y = page.getMediaBox().getHeight() - 50;
                for (int i = 0; i < cols; i++) {
                    String header = tableModel.getColumnName(i);
                    float x = startX + colWidth * i;
                    contentStream.beginText();
                    contentStream.setFont(font, 13);
                    contentStream.newLineAtOffset(x + cellMargin, y - cellMargin - 10); // Adjusted y coordinate
                    contentStream.showText(header);
                    contentStream.endText();

                    // Draw cell borders
                    contentStream.addRect(x, y - rowHeight, colWidth, rowHeight);
                    contentStream.stroke();
                }

                // Draw table rows
                for (int i = 0; i < rows; i++) {
                    y -= rowHeight;
                    for (int j = 0; j < cols; j++) {
                        String cellValue = tableModel.getValueAt(i, j).toString();
                        float x = startX + colWidth * j;
                        contentStream.beginText();
                        contentStream.setFont(font, 10);
                        contentStream.newLineAtOffset(x + cellMargin, y - cellMargin - 10); // Adjusted y coordinate
                        contentStream.showText(cellValue);
                        contentStream.endText();

                        // Draw cell borders
                        contentStream.addRect(x, y - rowHeight, colWidth, rowHeight);
                        contentStream.stroke();
                    }
                }

                // Close the content stream
                contentStream.close();

                // Let the user choose the save location
                JFileChooser fileChooser = new JFileChooser();
                int userSelection = fileChooser.showSaveDialog(Revenue.this);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    // Get the selected file
                    File fileToSave = fileChooser.getSelectedFile();

                    // Save the document to the selected file
                    document.save(fileToSave);

                    // Show a confirmation message
                    JOptionPane.showMessageDialog(null, "PDF file saved successfully!");
                }

                // Close the document
                document.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return panel;
    }



    private void showPanel(JPanel panel) {
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
