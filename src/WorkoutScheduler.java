package src;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class WorkoutScheduler {
    private JFrame frame;
    private JPanel mainPanel, bmiPanel, workoutPanel;
    private JTextField heightField, weightField;
    private JLabel bmiResultLabel, bmiCategoryLabel;
    private DefaultListModel<JCheckBox> todoListModel;
    private DefaultListModel<String> monthScheduleListModel;
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;
    private JPanel todoPanelList;
    private double userBMI;


    public WorkoutScheduler() {
        frame = new JFrame("Workout Scheduler");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        createBMIPanel();
        frame.add(bmiPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    //BMI Calculator Screen
    private void createBMIPanel() {
        bmiPanel = new JPanel(new GridBagLayout());
        bmiPanel.setBackground(new Color(0xE6E6FA)); // Light lavender background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel titleLabel = new JLabel("BMI Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        bmiPanel.add(titleLabel, gbc);

        JLabel heightLabel = new JLabel("Height (cm):");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        bmiPanel.add(heightLabel, gbc);

        heightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 1;
        bmiPanel.add(heightField, gbc);

        JLabel weightLabel = new JLabel("Weight (kg):");
        gbc.gridx = 0;
        gbc.gridy = 2;
        bmiPanel.add(weightLabel, gbc);

        weightField = new JTextField(10);
        gbc.gridx = 1;
        gbc.gridy = 2;
        bmiPanel.add(weightField, gbc);

        JButton calculateButton = new JButton("Calculate BMI");
        calculateButton.addActionListener(e -> calculateBMI());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        bmiPanel.add(calculateButton, gbc);

        bmiResultLabel = new JLabel("BMI: ");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        bmiPanel.add(bmiResultLabel, gbc);

        bmiCategoryLabel = new JLabel("Category: ");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        bmiPanel.add(bmiCategoryLabel, gbc);
    }

    //Segment to calculate BMI
    private void calculateBMI() {
        try {
            double height = Double.parseDouble(heightField.getText()) / 100; // convert cm to m
            double weight = Double.parseDouble(weightField.getText());
            userBMI = weight / (height * height);

            bmiResultLabel.setText(String.format("BMI: %.2f", userBMI));
            String category = getBMICategory(userBMI);
            bmiCategoryLabel.setText("Category: " + category);

            int choice = JOptionPane.showConfirmDialog(frame, 
                "Your BMI is " + String.format("%.2f", userBMI) + " (" + category + ").\nWould you like to see workout suggestions?", 
                "BMI Result", JOptionPane.YES_NO_OPTION);
            
            if (choice == JOptionPane.YES_OPTION) {
                showWorkoutSuggestions();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers for height and weight.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Underweight";
        else if (bmi < 25) return "Normal weight";
        else if (bmi < 30) return "Overweight";
        else return "Obese";
    }

    private void showWorkoutSuggestions() {
        frame.getContentPane().removeAll();
        createWorkoutPanel();
        frame.add(workoutPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void createWorkoutPanel() {
        workoutPanel = new JPanel(new BorderLayout());
        workoutPanel.setBackground(new Color(0xF0F8FF)); // Light blue background

        JLabel titleLabel = new JLabel("Suggested Workouts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        workoutPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel suggestionsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        suggestionsPanel.setBackground(new Color(0xF0F8FF));

        ArrayList<String> suggestions = getSuggestedWorkouts();
        for (String workout : suggestions) {
            JCheckBox workoutCheckBox = new JCheckBox(workout);
            suggestionsPanel.add(workoutCheckBox);
        }

        JScrollPane scrollPane = new JScrollPane(suggestionsPanel);
        workoutPanel.add(scrollPane, BorderLayout.CENTER);

        JButton addSelectedButton = new JButton("Add Selected Workouts");
        addSelectedButton.addActionListener(e -> addSelectedWorkouts(suggestionsPanel));
        workoutPanel.add(addSelectedButton, BorderLayout.SOUTH);
    }

    //Suggestions for weight class
    private ArrayList<String> getSuggestedWorkouts() {
        ArrayList<String> suggestions = new ArrayList<>();
        if (userBMI < 18.5) {
            suggestions.add("Strength Training: Bodyweight exercises (3 sets, 10 reps)");
            suggestions.add("Protein-rich meal planning");
            suggestions.add("Yoga for flexibility (30 minutes)");
        } else if (userBMI < 25) {
            suggestions.add("Cardio: Jogging (30 minutes)");
            suggestions.add("Strength Training: Weightlifting (3 sets, 12 reps)");
            suggestions.add("Swimming (45 minutes)");
        } else if (userBMI < 30) {
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
            JOptionPane.showMessageDialog(frame, "Please select at least one workout.", "No Selection", JOptionPane.WARNING_MESSAGE);
        } else {
            createMainView(selectedWorkouts);
        }
    }

    //Main menu segment
    private void createMainView(ArrayList<String> selectedWorkouts) {
        todoListModel = new DefaultListModel<>();
        monthScheduleListModel = new DefaultListModel<>();
        historyListModel = new DefaultListModel<>();
        todoPanelList = new JPanel();
        todoPanelList.setLayout(new BoxLayout(todoPanelList, BoxLayout.Y_AXIS));
        todoPanelList.setBackground(new Color(0xF5EFE7));

        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(new Color(0xd7e9e6));

        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        for (String workout : selectedWorkouts) {
            addWorkoutToTodoList(workout);
        }

        frame.getContentPane().removeAll();
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));
        leftPanel.setBackground(new Color(0xd5b1c8));

        JPanel monthSchedulePanel = new JPanel(new BorderLayout());
        JLabel monthLabel = new JLabel("Today's Schedule", SwingConstants.CENTER);
        JList<String> monthScheduleList = new JList<>(monthScheduleListModel);
        monthSchedulePanel.add(monthLabel, BorderLayout.NORTH);
        monthSchedulePanel.add(new JScrollPane(monthScheduleList), BorderLayout.CENTER);
        monthSchedulePanel.setBackground(new Color(0xd5b1c8));

        JPanel historyPanel = new JPanel(new BorderLayout());
        JLabel historyLabel = new JLabel("History", SwingConstants.CENTER);
        historyList = new JList<>(historyListModel);
        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);
        historyPanel.setBackground(new Color(0xdfeaa6));

        JButton restoreButton = new JButton("Restore Selected");
        restoreButton.addActionListener(e -> restoreSelectedTask());
        historyPanel.add(restoreButton, BorderLayout.SOUTH);

        leftPanel.add(monthSchedulePanel);
        leftPanel.add(historyPanel);

        return leftPanel;
    }

    //Buttons for Tasks
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        JLabel todoLabel = new JLabel("To-do Workouts", SwingConstants.CENTER);
        JScrollPane todoScrollPane = new JScrollPane(todoPanelList);
        rightPanel.add(todoLabel, BorderLayout.NORTH);
        rightPanel.add(todoScrollPane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));
        JButton addTaskButton = new JButton("Add Task");
        JButton editTaskButton = new JButton("Edit Task");
        JButton finishTaskButton = new JButton("Finish Task");
        JButton viewTaskButton = new JButton("View Task");

        addTaskButton.addActionListener(e -> switchToAddTaskView());
        editTaskButton.addActionListener(e -> editSelectedTask());
        finishTaskButton.addActionListener(e -> finishSelectedTask());
        viewTaskButton.addActionListener(e -> viewSelectedTask());

        buttonsPanel.add(addTaskButton);
        buttonsPanel.add(editTaskButton);
        buttonsPanel.add(viewTaskButton);
        buttonsPanel.add(finishTaskButton);

        rightPanel.add(buttonsPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    private void addWorkoutToTodoList(String workout) {
        JCheckBox workoutCheckBox = new JCheckBox(workout);
        todoListModel.addElement(workoutCheckBox);
        todoPanelList.add(workoutCheckBox);
        monthScheduleListModel.addElement(workout);
        todoPanelList.revalidate();
        todoPanelList.repaint();
    }

    //For Add Task Window
    private void switchToAddTaskView() {
        JPanel addTaskPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        addTaskPanel.setBackground(new Color(0xd7e9e6));
        addTaskPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameField = new JTextField();
        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Strength Training", "Cardio", "Yoga", "Flexibility", "Balance"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);

        JLabel setsRepsTimerLabel = new JLabel("Sets / Reps / Timer (min):");
        JPanel setsRepsTimerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        SpinnerNumberModel setsModel = new SpinnerNumberModel(1, 1, 10, 1);
        JSpinner setsSpinner = new JSpinner(setsModel);
        SpinnerNumberModel repsModel = new SpinnerNumberModel(1, 1, 50, 1);
        JSpinner repsSpinner = new JSpinner(repsModel);
        SpinnerNumberModel timerModel = new SpinnerNumberModel(1, 1, 120, 1);
        JSpinner timerSpinner = new JSpinner(timerModel);

        setsRepsTimerPanel.add(setsSpinner);
        setsRepsTimerPanel.add(repsSpinner);
        setsRepsTimerPanel.add(timerSpinner);

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        confirmButton.addActionListener(e -> addTask(nameField, descField, categoryComboBox, setsSpinner, repsSpinner, timerSpinner));
        cancelButton.addActionListener(e -> switchToMainView());

        addTaskPanel.add(nameLabel);
        addTaskPanel.add(nameField);
        addTaskPanel.add(descLabel);
        addTaskPanel.add(descField);
        addTaskPanel.add(categoryLabel);
        addTaskPanel.add(categoryComboBox);
        addTaskPanel.add(setsRepsTimerLabel);
        addTaskPanel.add(setsRepsTimerPanel);
        addTaskPanel.add(confirmButton);
        addTaskPanel.add(cancelButton);

        frame.getContentPane().removeAll();
        frame.add(addTaskPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void addTask(JTextField nameField, JTextField descField, JComboBox<String> categoryComboBox,
                         JSpinner setsSpinner, JSpinner repsSpinner, JSpinner timerSpinner) {
        String taskName = nameField.getText().trim();
        String taskDesc = descField.getText().trim();
        String taskCategory = categoryComboBox.getSelectedItem().toString();
        int sets = (int) setsSpinner.getValue();
        int reps = (int) repsSpinner.getValue();
        int timer = (int) timerSpinner.getValue();

        if (!taskName.isEmpty()) {
            String taskDetails = taskName + " - " + taskDesc + " (" + taskCategory + ") | Sets: " + sets +
                    ", Reps: " + reps + ", Timer: " + timer + " min";

            addWorkoutToTodoList(taskDetails);
            switchToMainView();
        } else {
            JOptionPane.showMessageDialog(frame, "Task name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Edit Task Window
    private void editSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        if (selectedTasks.size() == 1) {
            JCheckBox selectedTask = selectedTasks.get(0);
            String taskText = selectedTask.getText();

            JTextField nameField = new JTextField();
            JTextField descField = new JTextField();
            JSpinner setsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            JSpinner repsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
            JSpinner timerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 120, 1));

            Object[] editFields = {"Task Name:", nameField, "Description:", descField,
                    "Sets:", setsSpinner, "Reps:", repsSpinner, "Timer (min):", timerSpinner};

            int result = JOptionPane.showConfirmDialog(frame, editFields, "Edit Task", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                String newTask = nameField.getText() + " - " + descField.getText() + " | Sets: " +
                        setsSpinner.getValue() + ", Reps: " + repsSpinner.getValue() +
                        ", Timer: " + timerSpinner.getValue() + " min";

                selectedTask.setText(newTask);
                monthScheduleListModel.setElementAt(newTask, monthScheduleListModel.indexOf(taskText));

                todoPanelList.revalidate();
                todoPanelList.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select one task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Finish Task
    private void finishSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        for (JCheckBox selectedTask : selectedTasks) {
            String taskDetails = selectedTask.getText();
            todoListModel.removeElement(selectedTask);
            todoPanelList.remove(selectedTask);
            monthScheduleListModel.removeElement(taskDetails);
            
            historyListModel.addElement(taskDetails);
        }
        todoPanelList.revalidate();
        todoPanelList.repaint();
    }

    //View Task and Timer
    private void viewSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        if (selectedTasks.size() == 1) {
            JCheckBox selectedTask = selectedTasks.get(0);
            String taskDetails = selectedTask.getText();

            int timerValue = extractTimerFromTask(taskDetails);

            JOptionPane.showMessageDialog(frame, "Task Details:\n" + taskDetails, "View Task",
                    JOptionPane.INFORMATION_MESSAGE);

            int startTimer = JOptionPane.showConfirmDialog(frame,
                    "Start Timer for " + timerValue + " minutes?", "Start Workout",
                    JOptionPane.YES_NO_OPTION);

            if (startTimer == JOptionPane.YES_OPTION) {
                startWorkoutTimer(timerValue);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select one task to view.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extractTimerFromTask(String taskDetails) {
        String[] parts = taskDetails.split(", Timer:");
        return Integer.parseInt(parts[1].trim().split(" ")[0]);
    }

    private void startWorkoutTimer(int minutes) {
        int totalSeconds = minutes * 60;
    
        JFrame timerFrame = new JFrame("Workout Timer");
        timerFrame.setSize(300, 150);
        timerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JLabel timerLabel = new JLabel("", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerFrame.add(timerLabel, BorderLayout.CENTER);
    
        timerFrame.setVisible(true);
    
        Timer timer = new Timer(1000, new ActionListener() {
            int secondsLeft = totalSeconds;
    
            @Override
            public void actionPerformed(ActionEvent e) {
                if (secondsLeft > 0) {
                    int minutes = secondsLeft / 60;
                    int seconds = secondsLeft % 60;
                    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                    secondsLeft--;
                } else {
                    ((Timer) e.getSource()).stop();
                    timerLabel.setText("Workout Complete!");
                    JOptionPane.showMessageDialog(timerFrame, "Great job! Workout finished.", "Timer", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    
        timer.start();
    }

    private ArrayList<JCheckBox> getSelectedTasks() {
        ArrayList<JCheckBox> selectedTasks = new ArrayList<>();
        for (int i = 0; i < todoListModel.size(); i++) {
            JCheckBox taskCheckBox = todoListModel.getElementAt(i);
            if (taskCheckBox.isSelected()) {
                selectedTasks.add(taskCheckBox);
            }
        }
        return selectedTasks;
    }

    private void restoreSelectedTask() {
        String selectedTask = historyList.getSelectedValue();
        if (selectedTask != null) {
            historyListModel.removeElement(selectedTask);
            addWorkoutToTodoList(selectedTask);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a task to restore.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void switchToMainView() {
        frame.getContentPane().removeAll();
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WorkoutScheduler::new);
    }
}