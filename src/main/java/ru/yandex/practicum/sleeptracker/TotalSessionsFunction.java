package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class TotalSessionsFunction implements AnalyticsFunction {

    @Override
    public String getName() {
        return "Общее кол-во сеансов сна";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        return String.valueOf(sessions.size());
    }
}
