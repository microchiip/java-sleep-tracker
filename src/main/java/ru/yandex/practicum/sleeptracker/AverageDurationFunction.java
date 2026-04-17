package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.Locale;

public class AverageDurationFunction implements AnalyticsFunction {

    @Override
    public String getName() {
        return "Средняя продолжительность сна (в мин)";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return "Нет данных";
        }
        double average = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0);
        return String.format(Locale.US, "%.1f", average);
    }
}
