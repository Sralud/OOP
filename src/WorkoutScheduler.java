package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WorkoutScheduler {
    private JFrame frame;
    private BMICalculator bmiCalculator;
    private WorkoutSuggester workoutSuggester;
    private MainView mainView;

    public WorkoutScheduler() {
        frame = new JFrame("Workout Scheduler");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        bmiCalculator = new BMICalculator(this);
        workoutSuggester = new WorkoutSuggester(this);
        mainView = new MainView(this);

        showBMICalculator();
        frame.setVisible(true);
    }

    public void showBMICalculator() {
        frame.getContentPane().removeAll();
        frame.add(bmiCalculator.getPanel(), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public void showWorkoutSuggestions(double bmi) {
        frame.getContentPane().removeAll();
        frame.add(workoutSuggester.getPanel(bmi), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public void showMainView(ArrayList<String> selectedWorkouts) {
        frame.getContentPane().removeAll();
        frame.add(mainView.getPanel(selectedWorkouts), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public JFrame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WorkoutScheduler::new);
    }
}