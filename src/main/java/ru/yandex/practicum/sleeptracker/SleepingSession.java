package ru.yandex.practicum.sleeptracker;

import java.time.LocalDateTime;
import java.time.Duration;

public class SleepingSession {
    private final LocalDateTime fallAsleepTime;
    private final LocalDateTime wakeUpTime;
    private final SleepQuality quality;

    public SleepingSession(LocalDateTime fallAsleepTime, LocalDateTime wakeUpTime, SleepQuality quality) {
        this.fallAsleepTime = fallAsleepTime;
        this.wakeUpTime = wakeUpTime;
        this.quality = quality;
    }

    public LocalDateTime getFallAsleepTime() {
        return fallAsleepTime;
    }

    public LocalDateTime getWakeUpTime() {
        return wakeUpTime;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(fallAsleepTime, wakeUpTime).toMinutes();
    }
}
