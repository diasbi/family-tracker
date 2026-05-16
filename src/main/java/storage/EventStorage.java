package storage;

import models.Event;
import models.EventCategory;
import models.Priority;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventStorage {

    private Connection conn;
    private Map<LocalDate, List<Event>> events = new HashMap<>();

    String url = "jdbc:sqlite:eventsdb.db";

    public EventStorage()
    {
        initTable();
        loadEvents();
    }

    public void initTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS events (" +
                "id TEXT PRIMARY KEY, " +
                "title TEXT NOT NULL, " +
                "description TEXT, " +
                "category TEXT, " +
                "priority TEXT, " +
                "date TEXT" +
                ");";

        try
        {
            this.conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы: " + e.getMessage());
        }
    }
    public void updateEvent(Event e) {
        String sql = "UPDATE events SET title = ?," +
                "description = ?, category = ?, priority = ?," +
                " date = ? WHERE id = ?";


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {


            preparedStatement.setString(1, e.getTitle());
            preparedStatement.setString(2, e.getDescription());
            preparedStatement.setString(3, e.getCategory().toString());
            preparedStatement.setString(4, e.getPriority().toString());
            preparedStatement.setString(5, e.getEventDate().toString());
            preparedStatement.setString(6, e.getId());

            preparedStatement.executeUpdate();

            if (this.events != null) {
                for (List<Event> list : events.values()) {
                    list.removeIf(ev -> ev.getId().equals(e.getId()));
                }

                this.events
                        .computeIfAbsent(e.getEventDate(), d -> new ArrayList<>())
                        .add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Ошибка при обновлении события: " + ex.getMessage());
        }
    }


    public void addEvent(Event e)
    {
        String sql = "INSERT INTO events(id, title," +
                "description, category, priority, date) " +
                "VALUES(?, ?, ?, ?, ?, ?)";


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(2, e.getTitle());
            preparedStatement.setString(3, e.getDescription());
            preparedStatement.setString(4, e.getCategory().toString());
            preparedStatement.setString(5, e.getPriority().toString());
            preparedStatement.setString(6, e.getEventDate().toString());
            preparedStatement.setString(1, e.getId());

            preparedStatement.executeUpdate();

            if (this.events != null) {
                this.events
                        .computeIfAbsent(e.getEventDate(), d -> new ArrayList<>())
                        .add(e);
            }

        } catch (SQLException ex) {
            System.out.println("Ошибка при добавлении события: " + ex.getMessage());
        }
    }

    public void rmEvent(Event e)
    {
        String sql = "DELETE FROM events WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, e.getId());

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0 && this.events != null) {
                this.events.remove(e);
                System.out.println("Событие удалена из базы и списка.");
            }

        } catch (SQLException ex) {
            System.out.println("Ошибка при удалении события: " + ex.getMessage());
        }
    }

    public Map<LocalDate, List<Event>> getEvents()
    {
        return events;
    }


    public Map<LocalDate, List<Event>> loadEvents() {
        Map<LocalDate, List<Event>> loadedEvents = new HashMap<>();
        String sql = "SELECT * FROM events";

        try (Connection connection = DriverManager.getConnection(url);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String priority = rs.getString("priority");
                String date = rs.getString("date");
                Event event = new Event(title, description, EventCategory.valueOf(category), Priority.valueOf(priority), LocalDate.parse(date));
                event.setId(id);

                loadedEvents
                        .computeIfAbsent(event.getEventDate(), d -> new ArrayList<>())
                        .add(event);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке событий: " + e.getMessage());
        }

        this.events = loadedEvents;
        return loadedEvents;
    }

}

