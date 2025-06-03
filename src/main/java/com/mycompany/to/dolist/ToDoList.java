package com.mycompany.to.dolist;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ToDoList extends JFrame implements ActionListener {
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private JTextField taskInput;
    private JButton addButton, deleteButton, editButton, markDoneButton, saveButton, loadButton, clearAllButton;

    public ToDoList() {
        setTitle("Advanced To-Do List");
        setSize(500, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Task input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        taskInput = new JTextField();
        addButton = new JButton("Add Task");
        addButton.addActionListener(this);
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        // Task list
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        deleteButton = new JButton("Delete Task");
        editButton = new JButton("Edit Task");
        markDoneButton = new JButton("Mark as Done");
        saveButton = new JButton("Save Tasks");
        loadButton = new JButton("Load Tasks");
        clearAllButton = new JButton("Clear All");

        for (JButton btn : new JButton[]{deleteButton, editButton, markDoneButton, saveButton, loadButton, clearAllButton}) {
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        // Add to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                taskListModel.addElement(task);
                taskInput.setText("");
            } else {
                showMessage("Please enter a task.");
            }
        } else if (e.getSource() == deleteButton) {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                taskListModel.remove(index);
            } else {
                showMessage("Please select a task to delete.");
            }
        } else if (e.getSource() == editButton) {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String current = taskListModel.get(index);
                String edited = JOptionPane.showInputDialog(this, "Edit Task:", current);
                if (edited != null && !edited.trim().isEmpty()) {
                    taskListModel.set(index, edited.trim());
                }
            } else {
                showMessage("Please select a task to edit.");
            }
        } else if (e.getSource() == markDoneButton) {
            int index = taskList.getSelectedIndex();
            if (index != -1) {
                String task = taskListModel.get(index);
                if (!task.startsWith("✔ ")) {
                    taskListModel.set(index, "✔ " + task);
                }
            } else {
                showMessage("Please select a task to mark as done.");
            }
        } else if (e.getSource() == saveButton) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("tasks.txt"))) {
                for (int i = 0; i < taskListModel.size(); i++) {
                    writer.write(taskListModel.get(i));
                    writer.newLine();
                }
                showMessage("Tasks saved to tasks.txt");
            } catch (IOException ex) {
                showMessage("Error saving tasks.");
            }
        } else if (e.getSource() == loadButton) {
            try (BufferedReader reader = new BufferedReader(new FileReader("tasks.txt"))) {
                taskListModel.clear();
                String line;
                while ((line = reader.readLine()) != null) {
                    taskListModel.addElement(line);
                }
                showMessage("Tasks loaded from tasks.txt");
            } catch (IOException ex) {
                showMessage("Error loading tasks.");
            }
        } else if (e.getSource() == clearAllButton) {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure to clear all tasks?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                taskListModel.clear();
            }
        }
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        new ToDoList();
    }
}
