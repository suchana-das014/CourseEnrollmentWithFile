import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CourseEnrollment extends JFrame {
    private JTextField nameField, idField, numCoursesField, totalCreditsField;
    private JComboBox<String> programCombo, batchCombo, registrationStatusCombo, bloodGroupCombo;
    private JRadioButton summerRadio, springRadio, autumnRadio, maleRadio, femaleRadio, otherRadio;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    public CourseEnrollment() {
        super("Course Enrollment Form");

        // Set the layout for the frame
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // Add some space between fields
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label and Text Field for Name
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        add(nameField, gbc);

        // Label and Text Field for ID
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("ID:"), gbc);
        idField = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        add(idField, gbc);

        // Semester Selection
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Semester:"), gbc);
        JPanel semesterPanel = new JPanel();
        summerRadio = new JRadioButton("Summer");
        springRadio = new JRadioButton("Spring");
        autumnRadio = new JRadioButton("Autumn");

        ButtonGroup semesterGroup = new ButtonGroup();
        semesterGroup.add(summerRadio);
        semesterGroup.add(springRadio);
        semesterGroup.add(autumnRadio);

        semesterPanel.add(summerRadio);
        semesterPanel.add(springRadio);
        semesterPanel.add(autumnRadio);
        gbc.gridx = 1; gbc.gridy = 2;
        add(semesterPanel, gbc);

        // Program ComboBox
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Program Name:"), gbc);
        programCombo = new JComboBox<>(new String[]{"", "BSc in SWE", "BSc in CSE", "BSc in EEE", "Economics", "English", "LLB"});
        programCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBatchOptions();
            }
        });
        gbc.gridx = 1; gbc.gridy = 3;
        add(programCombo, gbc);

        // Batch ComboBox
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Batch:"), gbc);
        batchCombo = new JComboBox<>();
        batchCombo.addItem("");
        gbc.gridx = 1; gbc.gridy = 4;
        add(batchCombo, gbc);

        // Number of Courses
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Number of Courses:"), gbc);
        numCoursesField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 5;
        add(numCoursesField, gbc);

        // Total Credits
        gbc.gridx = 0; gbc.gridy = 6;
        add(new JLabel("Total Credits:"), gbc);
        totalCreditsField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 6;
        add(totalCreditsField, gbc);

        // Registration Status
        gbc.gridx = 0; gbc.gridy = 7;
        add(new JLabel("Registration Status:"), gbc);
        registrationStatusCombo = new JComboBox<>(new String[]{"", "Retake", "Supple", "Regular"});
        gbc.gridx = 1; gbc.gridy = 7;
        add(registrationStatusCombo, gbc);

        // Gender Selection
        gbc.gridx = 0; gbc.gridy = 8;
        add(new JLabel("Gender:"), gbc);
        JPanel genderPanel = new JPanel();
        maleRadio = new JRadioButton("Male");
        femaleRadio = new JRadioButton("Female");
        otherRadio = new JRadioButton("Other");

        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        genderPanel.add(maleRadio);
        genderPanel.add(femaleRadio);
        genderPanel.add(otherRadio);
        gbc.gridx = 1; gbc.gridy = 8;
        add(genderPanel, gbc);

        // Blood Group Selection
        gbc.gridx = 0; gbc.gridy = 9;
        add(new JLabel("Blood Group:"), gbc);
        bloodGroupCombo = new JComboBox<>(new String[]{"", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"});
        gbc.gridx = 1; gbc.gridy = 9;
        add(bloodGroupCombo, gbc);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        gbc.gridx = 0; gbc.gridy = 10; gbc.gridwidth = 2;
        add(submitButton, gbc);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitEnrollment();
            }
        });

        // Create the JTable
        String[] columnNames = {"Name", "ID", "Program", "Batch", "Semester", "Num Courses", "Total Credits", "Status", "Gender", "Blood Group"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        dataTable.setPreferredScrollableViewportSize(new Dimension(700, 200));  // Adjust the height
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);  // Auto resize columns

        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(700, 200));  // Scrollable table

        gbc.gridx = 0; gbc.gridy = 11; gbc.gridwidth = 2;  // JTable spans two columns
        add(scrollPane, gbc);

        // Save Button (replacing Delete button)
        JButton saveButton = new JButton("Save");
        gbc.gridx = 0; gbc.gridy = 12; gbc.gridwidth = 2;
        add(saveButton, gbc);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveToFile();
            }
        });
    }

    private void updateBatchOptions() {
        String selectedProgram = (String) programCombo.getSelectedItem();
        batchCombo.removeAllItems();
        batchCombo.addItem("");  // Default empty value

        if (selectedProgram == null || selectedProgram.isEmpty()) {
            return;
        }

        switch (selectedProgram) {
            case "BSc in SWE":
                for (int i = 1; i <= 7; i++) {
                    batchCombo.addItem("Batch " + i);
                }
                break;
            case "BSc in CSE":
                for (int i = 55; i <= 61; i++) {
                    batchCombo.addItem("Batch " + i);
                }
                break;
            default:
                for (int i = 48; i <= 53; i++) {
                    batchCombo.addItem("Batch " + i);
                }
                break;
        }
    }

    private void submitEnrollment() {
        String name = nameField.getText();
        String id = idField.getText();
        String semester = summerRadio.isSelected() ? "Summer" :
                          springRadio.isSelected() ? "Spring" : "Autumn";
        String program = (String) programCombo.getSelectedItem();
        String batch = (String) batchCombo.getSelectedItem();
        String numCourses = numCoursesField.getText();
        String totalCredits = totalCreditsField.getText();
        String status = (String) registrationStatusCombo.getSelectedItem();
        String gender = maleRadio.isSelected() ? "Male" :
                        femaleRadio.isSelected() ? "Female" : "Other";
        String bloodGroup = (String) bloodGroupCombo.getSelectedItem();

        // Add the new data to the table
        tableModel.addRow(new Object[]{
            name, id, program, batch, semester, numCourses, totalCredits, status, gender, bloodGroup
        });
    }

    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("enrollment_data.txt"))) {
            int rowCount = tableModel.getRowCount();
            int columnCount = tableModel.getColumnCount();
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < columnCount; j++) {
                    writer.write(tableModel.getValueAt(i, j).toString());
                    if (j < columnCount - 1) {
                        writer.write(", ");
                    }
                }
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "Data saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CourseEnrollment().setVisible(true));
    }
}
