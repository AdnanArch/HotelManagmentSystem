package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class Feedback extends JFrame {
    private final JComboBox<Integer> starsComboBox;
    private final JTextArea reviewTextArea;

    Feedback() {
        setTitle("Feedback");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLayout(new FlowLayout());
        setResizable(false);
        setLocationRelativeTo(null);

        JLabel starsLabel = new JLabel("Select the number of stars:");
        starsLabel.setFont(new Font("Sans-serif", Font.PLAIN, 18));
        starsLabel.setPreferredSize(new Dimension(250, 50));
        add(starsLabel);

        Integer[] starOptions = {1, 2, 3, 4, 5};
        starsComboBox = new JComboBox<>(starOptions);
        starsComboBox.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        add(starsComboBox);

        reviewTextArea = new JTextArea("Enter your review here", 10, 30);
        reviewTextArea.setFont(new Font("Sans-serif", Font.PLAIN, 17));
        reviewTextArea.setLineWrap(true); // Enable line wrapping
        reviewTextArea.setWrapStyleWord(true); // Wrap at word boundaries
        JScrollPane scrollPane = new JScrollPane(reviewTextArea); // Add the text area to a scroll pane

        // Set preferred size for the scroll pane
        scrollPane.setPreferredSize(new Dimension(400, 250));

        add(scrollPane);

        RoundedButton submitButton = new RoundedButton("Submit");
        submitButton.addActionListener(e -> {
            Integer selectedStars = (Integer) starsComboBox.getSelectedItem(); // Cast to Integer

            if (selectedStars != null) {
                int stars = selectedStars;
                String review = reviewTextArea.getText();

                // You can do something with the selected stars and review here
                // For example, print them to the console
                System.out.println("Stars: " + stars);
                System.out.println("Review: " + review);
            } else {
                System.out.println("No stars selected.");
            }

            // You can also close the feedback window after submitting
            dispose();
        });

        add(submitButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Feedback::new);
    }
}
