import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class WorkoutScheduler {
    private JFrame frame;
    private JPanel mainPanel, addTaskPanel, todoPanelList;
    private DefaultListModel<JCheckBox> todoListModel;
    private DefaultListModel<String> monthScheduleListModel;
    private DefaultListModel<String> historyListModel;
    private JList<String> historyList;

    public WorkoutScheduler() {
        // Initialize JFrame
        frame = new JFrame("Workout Scheduler");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Main Panel
        createMainView();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void createMainView() {
        // Left: "This Month's Schedule" and "History"
        JPanel leftPanel = new JPanel(new GridLayout(2, 1));

        // Upper part: This Month's Schedule
        JPanel monthSchedulePanel = new JPanel(new BorderLayout());
        JLabel monthLabel = new JLabel("This Month's Schedule", SwingConstants.CENTER);
        monthScheduleListModel = new DefaultListModel<>();
        JList<String> monthScheduleList = new JList<>(monthScheduleListModel);
        monthSchedulePanel.add(monthLabel, BorderLayout.NORTH);
        monthSchedulePanel.add(new JScrollPane(monthScheduleList), BorderLayout.CENTER);

        // Lower part: History
        JPanel historyPanel = new JPanel(new BorderLayout());
        JLabel historyLabel = new JLabel("History", SwingConstants.CENTER);
        historyListModel = new DefaultListModel<>();
        historyList = new JList<>(historyListModel);
        historyPanel.add(historyLabel, BorderLayout.NORTH);
        historyPanel.add(new JScrollPane(historyList), BorderLayout.CENTER);

        // Add restore button for history items
        JButton restoreButton = new JButton("Restore Selected");
        restoreButton.addActionListener(e -> restoreSelectedTask());
        historyPanel.add(restoreButton, BorderLayout.SOUTH);

        leftPanel.add(monthSchedulePanel);
        leftPanel.add(historyPanel);

        // Right: "To-do Workouts"
        JPanel todoPanel = new JPanel(new BorderLayout());
        JLabel todoLabel = new JLabel("To-do Workouts", SwingConstants.CENTER);
        todoListModel = new DefaultListModel<>();
        todoPanelList = new JPanel();
        todoPanelList.setLayout(new BoxLayout(todoPanelList, BoxLayout.Y_AXIS));

        JScrollPane todoScrollPane = new JScrollPane(todoPanelList);
        todoPanel.add(todoLabel, BorderLayout.NORTH);
        todoPanel.add(todoScrollPane, BorderLayout.CENTER);

        // Buttons: Add, Edit, Finish Task, View Task
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

        todoPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Update main panel
        mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(leftPanel);
        mainPanel.add(todoPanel);
    }

    private void switchToAddTaskView() {
        addTaskPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        addTaskPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Task Name:");
        JTextField nameField = new JTextField();
        JLabel descLabel = new JLabel("Description:");
        JTextField descField = new JTextField();
        JLabel categoryLabel = new JLabel("Category:");
        String[] categories = {"Strength Training", "Cardio", "Yoga", "Flexibility", "Balance"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);

        JLabel setsRepsTimerLabel = new JLabel("Sets / Reps / Timer (min):");

        // Create a panel to align sets, reps, and timer horizontally
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

        frame.getContentPane().remove(mainPanel);
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

            JCheckBox taskCheckBox = new JCheckBox(taskDetails);
            todoListModel.addElement(taskCheckBox);
            todoPanelList.add(taskCheckBox);

            monthScheduleListModel.addElement(taskDetails);

            todoPanelList.revalidate();
            todoPanelList.repaint();

            switchToMainView();
        } else {
            JOptionPane.showMessageDialog(frame, "Task name cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void finishSelectedTask() {
        ArrayList<JCheckBox> selectedTasks = getSelectedTasks();
        for (JCheckBox selectedTask : selectedTasks) {
            String taskDetails = selectedTask.getText();
            todoListModel.removeElement(selectedTask);
            todoPanelList.remove(selectedTask);
            monthScheduleListModel.removeElement(taskDetails);
            
            // Add the finished task to the history
            historyListModel.addElement(taskDetails);
        }
        todoPanelList.revalidate();
        todoPanelList.repaint();
    }

    private void restoreSelectedTask() {
        String selectedTask = historyList.getSelectedValue();
        if (selectedTask != null) {
            historyListModel.removeElement(selectedTask);
            
            // Add back to todo list
            JCheckBox restoredTask = new JCheckBox(selectedTask);
            todoListModel.addElement(restoredTask);
            todoPanelList.add(restoredTask);
            
            // Update monthly schedule
            monthScheduleListModel.addElement(selectedTask);
            
            // Refresh UI
            todoPanelList.revalidate();
            todoPanelList.repaint();
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
