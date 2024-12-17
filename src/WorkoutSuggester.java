package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WorkoutSuggester {
    private WorkoutScheduler scheduler;
    private JPanel panel;

    public WorkoutSuggester(WorkoutScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JPanel getPanel(double bmi) {
        createPanel(bmi);
        return panel;
    }

    private void createPanel(double bmi) {
        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xF0F8FF));

        JLabel titleLabel = new JLabel("Suggested Workouts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel suggestionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        suggestionsPanel.setBackground(new Color(0xF0F8FF));

        ArrayList<String> suggestions = getSuggestedWorkouts(bmi);
        for (String workout : suggestions) {
            JCheckBox workoutCheckBox = new JCheckBox(workout);
            suggestionsPanel.add(workoutCheckBox);
        }

        JScrollPane scrollPane = new JScrollPane(suggestionsPanel);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton addSelectedButton = new JButton("Add Selected Workouts");
        addSelectedButton.addActionListener(e -> addSelectedWorkouts(suggestionsPanel));
        panel.add(addSelectedButton, BorderLayout.SOUTH);
    }

    private ArrayList<String> getSuggestedWorkouts(double bmi) {
        ArrayList<String> suggestions = new ArrayList<>();
        if (bmi < 18.5) {
            suggestions.add("Strength Training: Bodyweight exercises (3 sets, 10 reps)");
            suggestions.add("Protein-rich meal planning");
            suggestions.add("Yoga for flexibility (30 minutes)");
        } else if (bmi < 25) {
            suggestions.add("Cardio: Jogging (30 minutes)");
            suggestions.add("Strength Training: Weightlifting (3 sets, 12 reps)");
            suggestions.add("Swimming (45 minutes)");
        } else if (bmi < 30) {
            suggestions.add("High-Intensity Interval Training (HIIT) (20 minutes)");
            suggestions.add("Strength Training: Circuit training (4 sets, 15 reps)");
            suggestions.add("Cycling (45 minutes)");
        } else {
            suggestions.add("Walking (30 minutes)");
            suggestions.add("Water Aerobics (45 minutes)");
            suggestions.add("Light Strength Training (2 sets, 10 reps)");
        }
        return suggestions;
    }

    private void addSelectedWorkouts(JPanel suggestionsPanel) {
        ArrayList<String> selectedWorkouts = new ArrayList<>();
        for (Component c : suggestionsPanel.getComponents()) {
            if (c instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox) c;
                if (cb.isSelected()) {
                    selectedWorkouts.add(cb.getText());
                }
            }
        }

        if (selectedWorkouts.isEmpty()) {
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Please select at least one workout.", "No Selection", JOptionPane.WARNING_MESSAGE);
        } else {
            scheduler.showMainView(selectedWorkouts);
        }
    }
}