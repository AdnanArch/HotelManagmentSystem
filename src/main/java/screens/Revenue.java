package screens;

import components.RoundedButton;
import components.RoundedTextField;
import connection.DatabaseConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Revenue extends JFrame implements ActionListener {
    private final JPanel rightPanel;
    private static RoundedButton specificRoomButton;
    private static RoundedButton roomRevenueOverTimeButton;
    private static RoundedButton monthlyRevenueButton;
    private static RoundedButton totalRevenueButton;


    public Revenue() {
        setTitle("Revenue Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1250, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create left panel for buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(350, getHeight()));

        leftPanel.setBackground(new Color(210, 225, 242));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create right panel for report panels
        rightPanel = new JPanel();
        rightPanel.setLayout(new CardLayout());
        rightPanel.setBackground(Color.WHITE);

        // Create buttons for reports
        specificRoomButton = createReportButton("Specific Room");
        roomRevenueOverTimeButton = createReportButton("Monthly Room Revenue");
        monthlyRevenueButton = createReportButton("Monthly Revenue");
        totalRevenueButton = createReportButton("Total Revenue");

        // Add buttons to the left panel
        leftPanel.add(specificRoomButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(roomRevenueOverTimeButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(monthlyRevenueButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(totalRevenueButton);

        // Add panels to the right panel
        JPanel specificRoomPanel = createSpecificRoomPanel();
        JPanel roomRevenueOverTimePanel = createRoomRevenueOverTimePanel();
        JPanel monthlyRevenuePanel = createMonthlyRevenuePanel();
        JPanel totalRevenuePanel = createTotalRevenuePanel();

        rightPanel.add(specificRoomPanel, "Specific Room Report");
        rightPanel.add(roomRevenueOverTimePanel, "Room Revenue Over Time Report");
        rightPanel.add(monthlyRevenuePanel, "Monthly Revenue Report");
        rightPanel.add(totalRevenuePanel, "Total Revenue Report");

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Button action listeners to show corresponding report panel
        specificRoomButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) rightPanel.getLayout();
            layout.show(rightPanel, "Specific Room Report");
        });

        roomRevenueOverTimeButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) rightPanel.getLayout();
            layout.show(rightPanel, "Room Revenue Over Time Report");
        });

        monthlyRevenueButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) rightPanel.getLayout();
            layout.show(rightPanel, "Monthly Revenue Report");
        });

        totalRevenueButton.addActionListener(e -> {
            CardLayout layout = (CardLayout) rightPanel.getLayout();
            layout.show(rightPanel, "Total Revenue Report");
        });

        setVisible(true);
    }

    private RoundedButton createReportButton(String text) {
        RoundedButton button = new RoundedButton(text);
        button.setPreferredSize(new Dimension(280, 50));
        button.setMaximumSize(new Dimension(280, 50));
        button.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        button.setForeground(new Color(255, 255, 255));
        button.setBackground(new Color(52, 152, 219));
        button.setFocusable(false);
        return button;
    }

    private JPanel createSpecificRoomPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create input fields and button for specific room report
        RoundedTextField roomNumberTextField = new RoundedTextField();
        roomNumberTextField.setPreferredSize(new Dimension(100, 30));
        roomNumberTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField monthTextField = new RoundedTextField();
        monthTextField.setPreferredSize(new Dimension(100, 30));
        monthTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField yearTextField = new RoundedTextField();
        yearTextField.setPreferredSize(new Dimension(100, 30));
        yearTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setMaximumSize(new Dimension(120, 35));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(52, 152, 219));

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
                    jComponent.setFont(new Font("Sans-serif", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(30); // Set the desired row height
        table.getTableHeader().setFont(new Font("Sans-serif", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        RoundedButton printButton = new RoundedButton("Print Report");
        printButton.setPreferredSize(new Dimension(150, 40));
        printButton.setMaximumSize(new Dimension(150, 40));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(52, 152, 219));

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel roomLabel = new JLabel("Room Number: ");
        roomLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        inputPanel.add(roomLabel);
        inputPanel.add(roomNumberTextField);

        inputPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing

        JLabel monthLabel = new JLabel("Month:");
        monthLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        inputPanel.add(monthLabel);
        inputPanel.add(monthTextField);

        inputPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Add spacing

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        inputPanel.add(yearLabel);
        inputPanel.add(yearTextField);

        inputPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add spacing

        inputPanel.add(submitButton);

        // Create panel for print button
        JPanel printButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printButtonPanel.setBackground(Color.WHITE);
        printButtonPanel.add(printButton);

        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButtonPanel, BorderLayout.SOUTH);

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

                double totalRevenue = 0.0;

                boolean dataExists = statement.getBoolean(4);

                if (dataExists) {
                    tableModel.setRowCount(0);
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

                        totalRevenue += revenue;
                    }
                    resultSet.close();

                    tableModel.addRow(new Object[]{"Total Revenue", "", "", "", totalRevenue});

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
        RoundedTextField roomNumberTextField = new RoundedTextField();
        roomNumberTextField.setPreferredSize(new Dimension(100, 30));
        roomNumberTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField monthTextField = new RoundedTextField();
        monthTextField.setPreferredSize(new Dimension(100, 30));
        monthTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField yearTextField = new RoundedTextField();
        yearTextField.setPreferredSize(new Dimension(100, 30));
        yearTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setMaximumSize(new Dimension(120, 35));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(52, 152, 219));

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
                    jComponent.setFont(new Font("Sans-serif", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(30); // Set the desired row height
        table.getTableHeader().setFont(new Font("Sans-serif", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        RoundedButton printButton = new RoundedButton("Print Report");
        printButton.setPreferredSize(new Dimension(150, 40));
        printButton.setMaximumSize(new Dimension(150, 40));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(52, 152, 219));

        // Create panel for input fields and submit button
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel roomLabel = new JLabel("Room Number: ");
        roomLabel.setFont(new Font("Sans-serif", Font.BOLD, 16));
        inputPanel.add(roomLabel);
        inputPanel.add(roomNumberTextField);

        inputPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Add spacing

        inputPanel.add(submitButton);

        // Create panel for print button
        JPanel printButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printButtonPanel.setBackground(Color.WHITE);
        printButtonPanel.add(printButton);

        // Add components to the panel
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButtonPanel, BorderLayout.SOUTH);

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

                double totalRevenue = 0.0;

                boolean dataExists = statement.getBoolean(2);

                if (dataExists) {
                    tableModel.setRowCount(0);
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

                        totalRevenue += revenue;
                    }
                    resultSet.close();

                    tableModel.addRow(new Object[]{"Total Revenue", "", "", "", totalRevenue});

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

    private JPanel createMonthlyRevenuePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create input fields and button for specific room report
        RoundedTextField roomNumberTextField = new RoundedTextField();
        roomNumberTextField.setPreferredSize(new Dimension(100, 30));
        roomNumberTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField monthTextField = new RoundedTextField();
        monthTextField.setPreferredSize(new Dimension(100, 30));
        monthTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField yearTextField = new RoundedTextField();
        yearTextField.setPreferredSize(new Dimension(100, 30));
        yearTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setMaximumSize(new Dimension(120, 35));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(52, 152, 219));

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
                    jComponent.setFont(new Font("Sans-serif", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(30); // Set the desired row height
        table.getTableHeader().setFont(new Font("Sans-serif", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        RoundedButton printButton = new RoundedButton("Print Report");
        printButton.setPreferredSize(new Dimension(150, 40));
        printButton.setMaximumSize(new Dimension(150, 40));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(52, 152, 219));

        // Create panel for print button
        JPanel printButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printButtonPanel.setBackground(Color.WHITE);
        printButtonPanel.add(printButton);

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButtonPanel, BorderLayout.SOUTH);

        // Call the stored procedure and retrieve the data
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL get_monthly_revenue_report(?)}");

            statement.registerOutParameter(1, Types.BOOLEAN);
            // Execute the stored procedure
            statement.execute();

            double totalRevenue = 0.0;

            boolean dataExists = statement.getBoolean(1);

            if (dataExists) {
                tableModel.setRowCount(0);
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

                    totalRevenue += revenue;
                }
                resultSet.close();

                tableModel.addRow(new Object[]{"Total Revenue", "", "", "", totalRevenue});

            } else {
                JOptionPane.showMessageDialog(null, "No data found");
            }
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

        // Create input fields and button for specific room report
        RoundedTextField roomNumberTextField = new RoundedTextField();
        roomNumberTextField.setPreferredSize(new Dimension(100, 30));
        roomNumberTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField monthTextField = new RoundedTextField();
        monthTextField.setPreferredSize(new Dimension(100, 30));
        monthTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedTextField yearTextField = new RoundedTextField();
        yearTextField.setPreferredSize(new Dimension(100, 30));
        yearTextField.setFont(new Font("Sans-serif", Font.BOLD, 15));

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.setPreferredSize(new Dimension(120, 35));
        submitButton.setMaximumSize(new Dimension(120, 35));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(new Color(52, 152, 219));

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
                    jComponent.setFont(new Font("Sans-serif", Font.PLAIN, 16)); // Set the desired font size
                    jComponent.setBackground(Color.WHITE.brighter());
                }
                return component;
            }
        };

        table.setRowHeight(30); // Set the desired row height
        table.getTableHeader().setFont(new Font("Sans-serif", Font.BOLD, 16));
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(table);

        // Create button for printing report
        RoundedButton printButton = new RoundedButton("Print Report");
        printButton.setPreferredSize(new Dimension(150, 40));
        printButton.setMaximumSize(new Dimension(150, 40));
        printButton.setForeground(Color.WHITE);
        printButton.setBackground(new Color(52, 152, 219));

        // Create panel for print button
        JPanel printButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        printButtonPanel.setBackground(Color.WHITE);
        printButtonPanel.add(printButton);

        // Add components to the panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(printButtonPanel, BorderLayout.SOUTH);

        // Call the stored procedure and retrieve the data
        try {
            DatabaseConnection db = new DatabaseConnection();
            CallableStatement statement = db.connection.prepareCall("{CALL get_total_revenue_report(?)}");

            statement.registerOutParameter(1, Types.BOOLEAN);
            // Execute the stored procedure
            statement.execute();

            double totalRevenue = 0.0;  // Variable to store the total revenue

            boolean dataExists = statement.getBoolean(1);

            if (dataExists) {
                tableModel.setRowCount(0);
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

                    // Update the total revenue
                    totalRevenue += revenue;
                }
                resultSet.close();

                // Add a row with the total revenue to the table model
                tableModel.addRow(new Object[]{"Total Revenue", "", "", "", totalRevenue});


            } else {
                JOptionPane.showMessageDialog(null, "No data found");
            }
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

                    // Check if the file has ".pdf" extension, if not, append it to the file name
                    String filePath = fileToSave.getAbsolutePath();
                    if (!filePath.toLowerCase().endsWith(".pdf")) {
                        filePath += ".pdf";
                        fileToSave = new File(filePath);
                    }

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

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout layout = (CardLayout) rightPanel.getLayout();

        if (e.getSource() == specificRoomButton) {
            layout.show(rightPanel, "Specific Room Report");
        } else if (e.getSource() == roomRevenueOverTimeButton) {
            layout.show(rightPanel, "Room Revenue Over Time Report");
        } else if (e.getSource() == monthlyRevenueButton) {
            layout.show(rightPanel, "Monthly Revenue Report");
        } else if (e.getSource() == totalRevenueButton) {
            layout.show(rightPanel, "Total Revenue Report");
        }
    }
}