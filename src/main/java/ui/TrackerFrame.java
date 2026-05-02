package ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import models.Event;
import models.EventCategory;
import storage.EventStorage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


public class TrackerFrame extends JFrame {

    private EventStorage storage;
    private JTable tasksTable;
    private DefaultTableModel tableModel;
    private JComboBox<EventCategory> categoryFilter;
    private JComboBox<String> weekPicker;
    private JComboBox<String> monthPicker;

    private int themecnt;
    private final Color[][] THEMES = {
            { new Color(240, 240, 240), Color.BLACK }, // White
            { new Color(45, 45, 45), new Color(220, 220, 220) } // Dark
    };

    // Init Frame
    public TrackerFrame() {
        super("Family Tracker");

        storage = new EventStorage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 590);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();
        loadEvents();
    }

    private void initUI() {
        // Главная панель
        JPanel mainPanel = new JPanel(new BorderLayout());
        this.add(mainPanel);
        // Панель инструментов (поиск и фильтры)
        JPanel toolbarPanel = createToolbarPanel();
        mainPanel.add(toolbarPanel, BorderLayout.NORTH);

        // Таблица задач
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Панель кнопок
        JPanel buttonsPanel = createButtonsPanel();
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        changeTheme();

    }

    // UI creation
    private JPanel createToolbarPanel() {
        JPanel tbpanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        // TODO: add search later and automatically change the week and month
//        panel.add(new JLabel("Поиск:"));
//        searchField = new JTextField(15);
//        searchField.addKeyListener(new KeyAdapter() { // Use KeyAdapter for convenience
//            @Override
//            public void keyReleased(KeyEvent e) {
//                applyFilters();
//            }
//        });
//        panel.add(searchField);

        panel.add(new JLabel("Дата: "));
        String[] weeks = {"1-ая неделя", "2-ая неделя", "3-ая неделя", "4-ая неделя"};
        weekPicker = new JComboBox<>(weeks);
        weekPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        panel.add(weekPicker);

        String[] months = {"Январь", "Февраль", "Март", "Апрель","Май","Июнь","Июль","Август","Сентябрь","Октябрь","Ноябрь","Декабрь"};
        monthPicker = new JComboBox<>(months);
        monthPicker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });

        panel.add(monthPicker);

        // Фильтр по категориям
        panel.add(new JLabel("Категория:"));
        EventCategory[] categories = new EventCategory[EventCategory.values().length + 1];
        categories[0] = null; // "Все"
        for (int i = 0; i < EventCategory.values().length; i++) {
            categories[i + 1] = EventCategory.values()[i];
        }
        categoryFilter = new JComboBox<>(categories);
        categoryFilter.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null) {
                    value = "Все";
                } else {
                    value = ((EventCategory) value).getDisplayName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });
        categoryFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        panel.add(categoryFilter);


        JPanel panelTheme = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton applyFilterBtn = new JButton("Цвет");
        applyFilterBtn.addActionListener(e -> changeTheme());
        panelTheme.add(applyFilterBtn);

        tbpanel.add(panel, BorderLayout.WEST);
        tbpanel.add(panelTheme, BorderLayout.EAST);
        return tbpanel;
    }

    private JPanel createTablePanel() {

        JPanel panel = new JPanel(new BorderLayout());
        // Модель таблицы
        tableModel = new DefaultTableModel(
                new String[]{"Понедельник", "Вторник", "Среда","Четверг","Пятница", "Суббота", "Воскресенье"},
                0
        ) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == -1;
            }

        };


        tasksTable = new JTable(tableModel);
        tasksTable.setRowHeight(25);
        tasksTable.getTableHeader().setReorderingAllowed(false);
        tasksTable.setRowSelectionAllowed(false);
        tasksTable.setColumnSelectionAllowed(true);
        tasksTable.setCellSelectionEnabled(true);
        tasksTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tasksTable.rowAtPoint(e.getPoint());
                    int column = tasksTable.columnAtPoint(e.getPoint());

                    Object value = tableModel.getValueAt(row, column);

                    if (value instanceof Event event) {
                        openEditEventDialog();
                    }
                }
            }
        });
        tasksTable.getColumnModel().getColumn(0).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(1).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(2).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(3).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(4).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(5).setPreferredWidth(96);
        tasksTable.getColumnModel().getColumn(6).setPreferredWidth(96);

        JScrollPane scrollPane = new JScrollPane(tasksTable);;
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton addBtn = new JButton(" Добавить");
        addBtn.addActionListener(e -> openAddEventDialog());
        panel.add(addBtn);

        JButton editBtn = new JButton(" Редактировать");
        editBtn.addActionListener(e -> openEditEventDialog());
        panel.add(editBtn);

        JButton deleteBtn = new JButton(" Удалить");
        deleteBtn.addActionListener(e -> openDeleteEventDialog());
        panel.add(deleteBtn);

        return panel;
    }

    //Event dialogs
    private void openAddEventDialog() {
        EventDialog dialog = new EventDialog(this, null);
        Event newEvent = dialog.showDialog();
        if (newEvent != null) {
            storage.addEvent(newEvent);
            loadEvents();
        }
    }

    private void openEditEventDialog() {
        int selectedRow = tasksTable.getSelectedRow();
        int selectedCol = tasksTable.getSelectedColumn();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите событие для редактирования",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Object value = tableModel.getValueAt(selectedRow, selectedCol);

        if (value instanceof Event event) {
            EventDialog dialog = new EventDialog(this, event);
            Event editedEvent = dialog.showDialog();
            if (editedEvent != null) {
                storage.updateEvent(editedEvent);
                loadEvents();
            }
        }
    }

    private void openDeleteEventDialog() {
        int selectedRow = tasksTable.getSelectedRow();
        int selectedCol = tasksTable.getSelectedColumn();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Выберите событие для удаления",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Вы уверены, что хотите удалить это событие?",
                "Подтверждение удаления", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Object value = tableModel.getValueAt(selectedRow, selectedCol);

            if (value instanceof Event event) {
                storage.rmEvent(event);
                loadEvents();
            }
        }
    }

    //"Apply" functions
    private void applyFilters() {
        loadEvents();
    }
    private void applyTheme(Component c, Color bg, Color fg) {
        c.setBackground(bg);
        c.setForeground(fg);

        if (c instanceof Container cont) {
            for (Component child : cont.getComponents()) {
                applyTheme(child, bg, fg);
            }
        }
    }

    //Theme changing
    private void changeTheme(){
        applyTheme(this.getContentPane(), THEMES[themecnt%THEMES.length][0], THEMES[themecnt%THEMES.length][1]);
        if (themecnt % 2 == 0) {
            FlatLightLaf.setup();
        } else {
            FlatDarkLaf.setup();
        }

        SwingUtilities.updateComponentTreeUI(this);

        tasksTable.setShowHorizontalLines(true);
        tasksTable.setShowVerticalLines(true);


        tasksTable.setShowGrid(true);
        tasksTable.setGridColor(new Color(120, 120, 120, 100));

        themecnt++;
    }

    //Events loading
    private void loadEvents() {
        tableModel.setRowCount(0);

        Map<LocalDate, List<Event>> events = storage.getEvents();

        int maxEvents = 0;
        for (List<Event> list : events.values()) {
            if (list.size() > maxEvents) {
                maxEvents = list.size();
            }
        }

        for (int i = 0; i < maxEvents; i++) {

            Object[] row = new Object[7];

            for (Map.Entry<LocalDate, List<Event>> entry : events.entrySet()) {

                int dayIndex = entry.getKey().getDayOfWeek().getValue() - 1;
                List<Event> list = entry.getValue();

                if (i < list.size()) {
                    row[dayIndex] = list.get(i);
                } else {
                    row[dayIndex] = "";
                }
            }

            tableModel.addRow(row);
        }

        for (int i=0; i<18-maxEvents; i++) tableModel.addRow(new Object[7]);
    }
}

