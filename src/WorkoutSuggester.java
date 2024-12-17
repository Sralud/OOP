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

        JLabel titleLabel = new JLabel("Suggested Indoor Workouts", SwingConstants.CENTER);
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
            suggestions.add("Bodyweight Squats (Sets: 3, Reps: 12, Timer: 10 mins)");
            suggestions.add("Push-ups (Sets: 3, Reps: 10, Timer: 10 mins)");
            suggestions.add("Indoor Yoga (Set: 1, Timer: 30 mins)");
        } else if (bmi < 25) {
            suggestions.add("Jump Rope (Sets: 3, Reps: 2, Timer: 10 mins)");
            suggestions.add("Dumbbell Rows (Sets: 3, Reps: 12, Timer: 15 mins)");
            suggestions.add("Plank Hold (Sets: 3, Reps: 2, Timer: 5 mins)");
        } else if (bmi < 30) {
            suggestions.add("High Knees (Sets: 4, Reps: 2, Timer: 5 mins)");
            suggestions.add("Indoor Cycling (Set: 1, Rep: 1, Timer: 30 mins)");
            suggestions.add("Bodyweight Lunges (Sets: 3, Reps(per leg): 15, Timer: 15 mins)");
        } else {
            suggestions.add("Seated Leg Raises (Sets: 3, Reps: 12, Timer: 10 mins)");
            suggestions.add("Wall Push-ups (Sets: 3, Reps: 10, Timer: 10 mins)");
            suggestions.add("Gentle Stretching (Set: 1, Rep: 1, Timer: 20 mins)");
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