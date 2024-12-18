import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BMICalculator {
    private WorkoutScheduler scheduler;
    private JPanel panel;
    private JTextField heightField, weightField;
    private JLabel bmiResultLabel, bmiCategoryLabel;

    public BMICalculator(WorkoutScheduler scheduler) {
        this.scheduler = scheduler;
        createPanel();
    }

    private void createPanel() {
        panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(0xE6E6FA));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("BMI Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        JLabel heightLabel = new JLabel("Height (cm):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(heightLabel, gbc);

        heightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(heightField, gbc);

        JLabel weightLabel = new JLabel("Weight (kg):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(weightLabel, gbc);

        weightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(weightField, gbc);

        JButton calculateButton = new JButton("Calculate BMI");
        calculateButton.addActionListener(e -> calculateBMI());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(calculateButton, gbc);

        bmiResultLabel = new JLabel("BMI: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(bmiResultLabel, gbc);

        bmiCategoryLabel = new JLabel("Category: ");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(bmiCategoryLabel, gbc);
    }

    private void calculateBMI() {
        try {
            double height = Double.parseDouble(heightField.getText()) / 100;
            double weight = Double.parseDouble(weightField.getText());
            double bmi = weight / (height * height);

            bmiResultLabel.setText(String.format("BMI: %.2f", bmi));
            String category = getBMICategory(bmi);
            bmiCategoryLabel.setText("Category: " + category);

            int choice = JOptionPane.showConfirmDialog(scheduler.getFrame(), 
                "Your BMI is " + String.format("%.2f", bmi) + " (" + category + ").\nWould you like to see workout suggestions?", 
                "BMI Result", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                scheduler.showWorkoutSuggestions(bmi);
            } else {
                scheduler.showMainView(new ArrayList<>());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Please enter valid numbers for height and weight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal weight";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    public JPanel getPanel() {
        return panel;
    }
}