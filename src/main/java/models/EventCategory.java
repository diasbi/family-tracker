package models;

public enum EventCategory {
    WORK("Работа"),
    PERSONAL("Личное"),
    STUDY("Учёба"),
    OTHER("Прочое");

    private final String displayName;

    EventCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
