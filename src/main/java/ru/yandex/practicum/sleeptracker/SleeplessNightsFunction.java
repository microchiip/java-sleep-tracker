package ru.yandex.practicum.sleeptracker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.LongStream;

public class SleeplessNightsFunction implements AnalyticsFunction {

    @Override
    public String getName() {
        return "Бессонные ночи";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return "0";
        }

        LocalDate firstNight = getFirstNightDate(sessions.get(0));
        LocalDate lastNight = getLastNightDate(sessions.get(sessions.size() - 1));

        long daysBetween = ChronoUnit.DAYS.between(firstNight, lastNight);
        if (daysBetween < 0) {
            return "0";
        }

        long sleeplessCount = LongStream.rangeClosed(0, daysBetween)
                .mapToObj(firstNight::plusDays)
                .filter(nightDate -> isNightSleepless(nightDate, sessions))
                .count();

        return String.valueOf(sleeplessCount);
    }

    private boolean isNightSleepless(LocalDate nightDate, List<SleepingSession> sessions) {
        LocalDateTime nightStart = nightDate.atStartOfDay();
        LocalDateTime nightEnd = nightDate.atTime(6, 0);
        return sessions.stream()
                .noneMatch(s -> s.getFallAsleepTime().isBefore(nightEnd)
                        && nightStart.isBefore(s.getWakeUpTime()));
    }

    private LocalDate getFirstNightDate(SleepingSession firstSession) {
        LocalTime time = firstSession.getFallAsleepTime().toLocalTime();
        LocalDate date = firstSession.getFallAsleepTime().toLocalDate();
        if (time.isBefore(LocalTime.NOON)) {
            return date;
        } else {
            return date.plusDays(1);
        }
    }

    private LocalDate getLastNightDate(SleepingSession lastSession) {
        return lastSession.getWakeUpTime().toLocalDate();
    }
}
