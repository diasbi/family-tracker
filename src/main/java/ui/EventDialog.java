package ui;

import com.toedter.calendar.JDateChooser;
import models.Event;
import models.EventCategory;
import models.Priority;
import utils.EventValidator;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class EventDialog extends JDialog {

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<EventCategory> categoryCombo;
    private JComboBox<Priority> priorityCombo;
    private JSpinner dateSpinner;
    private JButton okBtn;
    private JButton cancelBtn;
    private JDateChooser dateChooser;

    private Event event;
    private boolean isConfirmed = false;

    public EventDialog(JFrame parent, Event event) {
        super(parent, "Добавить/Редактировать событие", true);

        this.event = event;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(parent);

        initUI();

        if (event != null) {
            loadTaskData();
        }
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Название
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Название:"), gbc);

        gbc.gridx = 1;
        titleField = new JTextField(25);
        mainPanel.add(titleField, gbc);

        // Описание
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Описание:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        descriptionArea = new JTextArea(5, 25);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);

        mainPanel.add(scrollPane, gbc);

        // Категория
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(new JLabel("Категория:"), gbc);

        gbc.gridx = 1;
        categoryCombo = new JComboBox<>(EventCategory.values());
        mainPanel.add(categoryCombo, gbc);


        // Приоритет
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Приоритет:"), gbc);

        gbc.gridx = 1;
        priorityCombo = new JComboBox<>(Priority.values());
        mainPanel.add(priorityCombo, gbc);

        // Дата
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Дата события:"), gbc);

        gbc.gridx = 1;
        dateChooser = new JDateChooser();
        dateChooser.setDate(new java.util.Date());
        mainPanel.add(dateChooser, gbc);


        // Кнопки
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        okBtn = new JButton("OK");
        okBtn.addActionListener(e -> confirmDialog());
        buttonsPanel.add(okBtn);

        cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(e -> cancelDialog());
        buttonsPanel.add(cancelBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private void loadTaskData() {
        if (event != null) {
            titleField.setText(event.getTitle());
            descriptionArea.setText(event.getDescription());
            categoryCombo.setSelectedItem(event.getCategory());
            priorityCombo.setSelectedItem(event.getPriority());
            dateChooser.setDate(java.sql.Date.valueOf(event.getEventDate()));
        }
    }
    private void confirmDialog() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        EventCategory category = (EventCategory) categoryCombo.getSelectedItem();
        Priority priority = (Priority) priorityCombo.getSelectedItem();
        java.util.Date utilDate = dateChooser.getDate();

        LocalDate date = utilDate
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        // Валидация
//        if (!EventValidator.isValidTitle(title)) {
//            showError("Название должно содержать минимум 2 символа");
//            return;
//        }
//
//        if (!EventValidator.isValidCategory(category)) {
//            showError("Выберите категорию");
//            return;
//        }
//
//        if(!EventValidator.isValidTags(tags)){
//            showError("Тэги должны быть разделены с помощью ',' и не быть пустыми");
//            return;
//        }
//
//        if (!EventValidator.isValidPriority(priority)) {
//            showError("Выберите приоритет");
//            return;
//        }


        // Создаём или обновляем задачу
        if (event == null) {
            event = new Event(title, description, category, priority, date);
        } else {
            event.setTitle(title);
            event.setDescription(description);
            event.setCategory(category);
            event.setPriority(priority);
            event.setEventDate(date);
        }

        isConfirmed = true;
        dispose();
    }

    private void cancelDialog() {
        isConfirmed = false;
        event = null;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Ошибка валидации",
                JOptionPane.ERROR_MESSAGE);
    }

    public Event showDialog() {
        setVisible(true);
        return isConfirmed ? event : null;
    }
}
