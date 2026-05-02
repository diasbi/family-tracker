package utils;

import models.Event;
import models.Priority;
import models.EventCategory;
import java.time.LocalDate;
import java.util.List;

public class EventValidator {

    public static boolean isValidTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return title.trim().length() >= 2;
    }

    public static boolean isValidTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return false;
        }

        for (String tag : tags) {
            if (tag.trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidDescription(String description) {
        return description != null;
    }

    public static boolean isValidDate(LocalDate dueDate) {
        if (dueDate == null) {
            return true;
        }
        return !dueDate.isBefore(LocalDate.now());
    }

    public static boolean isValidPriority(Priority priority) {
        return priority != null;
    }

    public static boolean isValidCategory(EventCategory category) {
        return category != null;
    }

    public static String validateTask(Event event) {
        if (event == null) {
            return "Задача не может быть null";
        }

        if (!isValidTitle(event.getTitle())) {
            return "Название должно быть минимум из 2 символов";
        }

        if (!isValidDescription(event.getDescription())) {
            return "Описание некорректно";
        }

        if (!isValidPriority(event.getPriority())) {
            return "Приоритет не должен быть пустым";
        }

        if (!isValidCategory(event.getCategory())) {
            return "Категория не должна быть пустой";
        }

        return "";
    }
}
