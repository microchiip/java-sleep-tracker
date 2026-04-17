package ru.yandex.practicum.sleeptracker;

import java.util.List;
import java.util.function.Function;

public interface AnalyticsFunction extends Function<List<SleepingSession>, String> {
    String getName();
}