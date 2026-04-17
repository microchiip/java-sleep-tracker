package ru.yandex.practicum.sleeptracker;

import java.util.List;

public class BadQualityCountFunction implements AnalyticsFunction {

    @Override
    public String getName() {
        return "Сеансы плохого сна";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        long count = sessions.stream()
                .filter(s -> s.getQuality() == SleepQuality.BAD)
                .count();
        return String.valueOf(count);
    }
}
