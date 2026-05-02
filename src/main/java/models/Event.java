package models;

import java.time.LocalDate;
import java.util.UUID;

public class Event {
    private String id;
    private String title;
    private String description;
    private EventCategory category;
    private Priority priority;
    private LocalDate eventDate;

    public Event(String title, String description, EventCategory category,
                 Priority priority,LocalDate eventDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.eventDate = eventDate;
    }

    public Event(String id, String title, String description, EventCategory category,
                 Priority priority,LocalDate eventDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.eventDate = eventDate;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public EventCategory getCategory() { return category; }
    public Priority getPriority() { return priority;}
    public LocalDate getEventDate() {return  eventDate;}

    public void setId(String id){this.id = id;}
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(EventCategory category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setEventDate(LocalDate eventDate) {this.eventDate = eventDate;}


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return title;
    }
}
