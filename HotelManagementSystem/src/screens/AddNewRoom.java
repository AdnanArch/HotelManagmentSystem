package screens;

import components.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static screens.RoomFunctions.insertRoomData;

public class AddNewRoom extends JFrame implements ActionListener {
    private static RoundedButton addButton;
    private static JComboBox<String> roomComboBox;
    private static JComboBox<Integer> roomCapacityComboBox;
    private static JTextField rentTextField;
    private static JComboBox<String> roomStatusComboBox;
    private static JTextArea roomDescriptionTextArea;

    public AddNewRoom() {
        super("New Room");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(400, 170, 1030, 500);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(58, 109, 122));
        add(mainPanel);

        JLabel addRoomLabel = new JLabel("Add New Room");
        addRoomLabel.setBounds(440, 20, 190, 40);
        addRoomLabel.setFont(new Font("Arial", Font.BOLD, 22));
        addRoomLabel.setForeground(Color.WHITE.brighter());
        mainPanel.add(addRoomLabel);

        JLabel roomTypeLabel = new JLabel("Room Type");
        roomTypeLabel.setBounds(110, 90, 200, 30);
        roomTypeLabel.setForeground(Color.WHITE.brighter());
        roomTypeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomTypeLabel);

        String[] roomTypes = {"Single", "Double", "Suite", "Family", "Deluxe", "Executive", "Penthouse", "Standard", "Economy"};
        roomComboBox = new JComboBox<>(roomTypes);
        roomComboBox.setBounds(240, 90, 260, 35);
        roomComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomComboBox);

        JLabel capacityLabel = new JLabel("Capacity");
        capacityLabel.setBounds(540, 90, 200, 30);
        capacityLabel.setForeground(Color.WHITE.brighter());
        capacityLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(capacityLabel);

        Integer[] capacityValues = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        roomCapacityComboBox = new JComboBox<>(capacityValues);
        roomCapacityComboBox.setBounds(650, 90, 260, 35);
        roomCapacityComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomCapacityComboBox);

        JLabel rentLabel = new JLabel("Rent");
        rentLabel.setBounds(110, 150, 200, 30);
        rentLabel.setForeground(Color.WHITE.brighter());
        rentLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(rentLabel);

        rentTextField = new JTextField();
        rentTextField.setBounds(240, 150, 260, 35);
        rentTextField.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(rentTextField);

        JLabel roomStatusLabel = new JLabel("Status");
        roomStatusLabel.setBounds(540, 150, 200, 30);
        roomStatusLabel.setForeground(Color.WHITE.brighter());
        roomStatusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomStatusLabel);

        String[] roomStatusOptions = {"Available", "Occupied", "Maintenance"};
        roomStatusComboBox = new JComboBox<>(roomStatusOptions);
        roomStatusComboBox.setBounds(650, 150, 260, 35);
        roomStatusComboBox.setFont(new Font("Arial", Font.PLAIN, 17));
        mainPanel.add(roomStatusComboBox);

        JLabel roomDescriptionLabel = new JLabel("Description");
        roomDescriptionLabel.setBounds(110, 210, 200, 30);
        roomDescriptionLabel.setForeground(Color.WHITE.brighter());
        roomDescriptionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(roomDescriptionLabel);

        roomDescriptionTextArea = new JTextArea();
        roomDescriptionTextArea.setFont(new Font("Arial", Font.PLAIN, 17));
        JScrollPane descriptionScrollPane = new JScrollPane(roomDescriptionTextArea);
        descriptionScrollPane.setBounds(240, 210, 670, 150);
        mainPanel.add(descriptionScrollPane);

        addButton = new RoundedButton("Add Room");
        addButton.setBounds(765, 380, 150, 40);
        addButton.setBackground(new Color(136, 208, 219));
        addButton.setFont(new Font("Arial", Font.BOLD, 17));
        addButton.setFocusable(false);
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource() == addButton) {
            String roomType = (String) roomComboBox.getSelectedItem();
            int roomCapacity = (int) Objects.requireNonNull(roomCapacityComboBox.getSelectedItem());
            double roomRent = Double.parseDouble(rentTextField.getText());
            String roomStatus = Objects.requireNonNull(roomStatusComboBox.getSelectedItem()).toString();
            String roomDescription = roomDescriptionTextArea.getText();

            boolean result = insertRoomData(roomType, roomCapacity, roomRent, roomStatus, roomDescription);
            if (result) {
                JOptionPane.showMessageDialog(this, "Successfully Room Added");
            }else {
                JOptionPane.showMessageDialog(this, "Invalid Information");
            }

            // Refresh the table with updated data
//            fetchAndRefreshRoomsDataFromDatabase();
        }
    }

    public static void main(String[] args) {
        new AddNewRoom();
    }
}
