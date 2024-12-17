//package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainView {
    private WorkoutScheduler scheduler;
    private JPanel panel;
    private DefaultListModel<JCheckBox> todoListModel;
    private DefaultListModel<String> monthScheduleListModel;
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;
    private JPanel todoPanelList;

    public MainView(WorkoutScheduler scheduler) {
        this.scheduler = scheduler;
    }

    public JPanel getPanel(ArrayList<String> selectedWorkouts) {
        if (panel == null) {
            createPanel(selectedWorkouts);
        } else {
            for (String workout : selectedWorkouts) {
                addWorkoutToTodoList(workout);
            }
        }
        return panel;
    }

    private void createPanel(ArrayList<String> selectedWorkouts) {
        todoListModel = new DefaultListModel<>();
        monthScheduleListModel = new DefaultListModel<>();
        historyListModel = new DefaultListModel<>();
        todoPanelList = new JPanel();
        todoPanelList.setLayout(new BoxLayout(todoPanelList, BoxLayout.Y_AXIS));
        todoPanelList.setBackground(new Color(0xF5EFE7));

        panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0xd7e9e6));

        JPanel contentPanel = new JPanel(new GridLayout(1, 2));
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createRightPanel();

        contentPanel.add(leftPanel);
        contentPanel.add(rightPanel);

        panel.add(contentPanel, BorderLayout.CENTER);
        
        JButton returnToBMIButton = new JButton("Return to BMI Calculator");
        returnToBMIButton.addActionListener(e -> scheduler.showBMICalculator());
        panel.add(returnToBMIButton, BorderLayout.NORTH);

        for (String workout : selectedWorkouts) {
            addWorkoutToTodoList(workout);
        }
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
        cancelButton.addActionListener(e -> scheduler.showMainView(new ArrayList<>()));

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

        scheduler.getFrame().getContentPane().removeAll();
        scheduler.getFrame().add(addTaskPanel, BorderLayout.CENTER);
        scheduler.getFrame().revalidate();
        scheduler.getFrame().repaint();
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
            scheduler.showMainView(new ArrayList<>());
        } else {
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Task name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        if (selectedTasks.size() == 1) {
            JCheckBox selectedTask = selectedTasks.get(0);
            String taskText = selectedTask.getText();

            JTextField nameField = new JTextField();
            JTextField descField = new JTextField();
            JSpinner setsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
            JSpinner repsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
            JSpinner timerSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1800, 1));

            Object[] editFields = {"Task Name:", nameField, "Description:", descField,
                    "Sets:", setsSpinner, "Reps:", repsSpinner, "Timer (min):", timerSpinner};

            int result = JOptionPane.showConfirmDialog(scheduler.getFrame(), editFields, "Edit Task", JOptionPane.OK_CANCEL_OPTION);

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
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Please select one task to edit.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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

    private void viewSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        if (selectedTasks.size() == 1) {
            JCheckBox selectedTask = selectedTasks.get(0);
            String taskDetails = selectedTask.getText();

        // Extract timer value
        int timerValue = extractTimerFromTask(taskDetails);

        JOptionPane.showMessageDialog(scheduler.getFrame(), "Task Details:\n" + taskDetails, "View Task",
                JOptionPane.INFORMATION_MESSAGE);

        if (timerValue > 0) {
            int startTimer = JOptionPane.showConfirmDialog(scheduler.getFrame(),
                    "Start Timer for " + timerValue + " minutes?", "Start Workout",
                    JOptionPane.YES_NO_OPTION);

            if (startTimer == JOptionPane.YES_OPTION) {
                startWorkoutTimer(timerValue);
            }
        }
        } else {
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Please select one task to view.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int extractTimerFromTask(String taskDetails) {
        try {
            String[] parts = taskDetails.split("Timer:");
            if (parts.length > 1) {
                return Integer.parseInt(parts[1].trim().split(" ")[0]);
            }
            // For suggested workouts format
            parts = taskDetails.split(", ");
            for (String part : parts) {
                if (part.toLowerCase().contains("min")) {
                    return Integer.parseInt(part.replaceAll("[^0-9]", ""));
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            // If we can't extract a timer value, return 0
        }
        return 0;
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
            JOptionPane.showMessageDialog(scheduler.getFrame(), "Please select a task to restore.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}