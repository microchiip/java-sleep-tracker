package ru.yandex.practicum.sleeptracker;

import java.util.Comparator;
import java.util.List;

public class MaxDurationFunction implements AnalyticsFunction {

    @Override
    public String getName() {
        return "Максимальное кол-во сна (в мин)";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        return sessions.stream()
                .map(SleepingSession::getDurationInMinutes)
                .max(Comparator.naturalOrder())
                .map(String::valueOf)
                .orElse("Нет данных");
    }
}
