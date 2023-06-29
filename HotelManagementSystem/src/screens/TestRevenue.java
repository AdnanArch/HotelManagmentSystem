package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestRevenue extends JFrame {
    private JPanel leftPanel;
    private JPanel rightPanel;

    private JPanel specificRoomPanel;
    private JPanel roomRevenueOverTimePanel;
    private JPanel monthlyRevenuePanel;
    private JPanel totalRevenuePanel;

    public TestRevenue() {
        setTitle("Revenue Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Create left panel for buttons
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(350, getHeight()));

//        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setBackground(new Color(210, 225, 242));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create right panel for report panels
        rightPanel = new JPanel();
        rightPanel.setLayout(new CardLayout());
        rightPanel.setBackground(Color.WHITE);

        // Create buttons for reports
        JButton specificRoomButton = createReportButton("Specific Room");
        JButton roomRevenueOverTimeButton = createReportButton("Monthly Room Revenue");
        JButton monthlyRevenueButton = createReportButton("Monthly Revenue");
        JButton totalRevenueButton = createReportButton("Total Revenue");

        // Add buttons to the left panel
        leftPanel.add(specificRoomButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(roomRevenueOverTimeButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(monthlyRevenueButton);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(totalRevenueButton);

        // Add panels to the right panel
        specificRoomPanel = createSpecificRoomPanel();
        roomRevenueOverTimePanel = createRoomRevenueOverTimePanel();
        monthlyRevenuePanel = createMonthlyRevenuePanel();
        totalRevenuePanel = createTotalRevenuePanel();

        rightPanel.add(specificRoomPanel, "Specific Room Report");
        rightPanel.add(roomRevenueOverTimePanel, "Room Revenue Over Time Report");
        rightPanel.add(monthlyRevenuePanel, "Monthly Revenue Report");
        rightPanel.add(totalRevenuePanel, "Total Revenue Report");

        // Add panels to the frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // Button action listeners to show corresponding report panel
        specificRoomButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) rightPanel.getLayout();
                layout.show(rightPanel, "Specific Room Report");
            }
        });

        roomRevenueOverTimeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) rightPanel.getLayout();
                layout.show(rightPanel, "Room Revenue Over Time Report");
            }
        });

        monthlyRevenueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) rightPanel.getLayout();
                layout.show(rightPanel, "Monthly Revenue Report");
            }
        });

        totalRevenueButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CardLayout layout = (CardLayout) rightPanel.getLayout();
                layout.show(rightPanel, "Total Revenue Report");
            }
        });

        setVisible(true);
    }

    private JButton createReportButton(String text) {
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
        // Create and return the specific room panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        // Customize the panel as per your requirement
        return panel;
    }

    private JPanel createRoomRevenueOverTimePanel() {
        // Create and return the room revenue over time panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        // Customize the panel as per your requirement
        return panel;
    }

    private JPanel createMonthlyRevenuePanel() {
        // Create and return the monthly revenue panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        // Customize the panel as per your requirement
        return panel;
    }

    private JPanel createTotalRevenuePanel() {
        // Create and return the total revenue panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        // Customize the panel as per your requirement
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TestRevenue();
            }
        });
    }
}
