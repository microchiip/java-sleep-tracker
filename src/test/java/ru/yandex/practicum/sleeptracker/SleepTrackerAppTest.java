package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SleepTrackerAppTest {

    private SleepingSession session(int d1, int m1, int h1, int min1,
                                    int d2, int m2, int h2, int min2,
                                    SleepQuality quality) {
        return new SleepingSession(
                LocalDateTime.of(2025, m1, d1, h1, min1),
                LocalDateTime.of(2025, m2, d2, h2, min2),
                quality
        );
    }

    private List<SleepingSession> sampleSessions() {
        return Arrays.asList(
                session(1, 10, 23, 0, 2, 10, 7, 0, SleepQuality.GOOD),
                session(2, 10, 23, 30, 3, 10, 6, 30, SleepQuality.NORMAL),
                session(3, 10, 14, 0, 3, 10, 15, 0, SleepQuality.NORMAL),
                session(3, 10, 23, 0, 4, 10, 8, 0, SleepQuality.BAD)
        );
    }

    //TotalSessionsFunction =

    @Test
    void totalSessions_withData() {
        assertEquals("4", new TotalSessionsFunction().apply(sampleSessions()));
    }

    @Test
    void totalSessions_empty() {
        assertEquals("0", new TotalSessionsFunction().apply(Collections.emptyList()));
    }

    // MinDurationFunction

    @Test
    void minDuration_withData() {
        // daytime nap 14:00-15:00 = 60 minutes is the shortest
        assertEquals("60", new MinDurationFunction().apply(sampleSessions()));
    }

    @Test
    void minDuration_empty() {
        assertEquals("Нет данных", new MinDurationFunction().apply(Collections.emptyList()));
    }

    // MaxDurationFunction

    @Test
    void maxDuration_withData() {
        assertEquals("540", new MaxDurationFunction().apply(sampleSessions()));
    }

    @Test
    void maxDuration_empty() {
        assertEquals("Нет данных", new MaxDurationFunction().apply(Collections.emptyList()));
    }

    //AverageDurationFunction

    @Test
    void averageDuration_withData() {
        // 480 + 420 + 60 + 540 = 1500 / 4 = 375.0
        assertEquals("375.0", new AverageDurationFunction().apply(sampleSessions()));
    }

    @Test
    void averageDuration_empty() {
        assertEquals("Нет данных", new AverageDurationFunction().apply(Collections.emptyList()));
    }

    //BadQualityCountFunction

    @Test
    void badQualityCount_withData() {
        assertEquals("1", new BadQualityCountFunction().apply(sampleSessions()));
    }

    @Test
    void badQualityCount_noBadSessions() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 0, 2, 10, 7, 0, SleepQuality.GOOD),
                session(2, 10, 23, 0, 3, 10, 7, 0, SleepQuality.NORMAL)
        );
        assertEquals("0", new BadQualityCountFunction().apply(sessions));
    }

    // SleeplessNightsFunction

    @Test
    void sleeplessNights_noSleeplessNights() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 0, 2, 10, 7, 0, SleepQuality.GOOD),
                session(2, 10, 23, 0, 3, 10, 7, 0, SleepQuality.GOOD),
                session(3, 10, 23, 0, 4, 10, 7, 0, SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(sessions));
    }

    @Test
    void sleeplessNights_oneGapNight() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 0, 2, 10, 7, 0, SleepQuality.GOOD),
                session(3, 10, 23, 0, 4, 10, 7, 0, SleepQuality.GOOD)
        );
        assertEquals("1", new SleeplessNightsFunction().apply(sessions));
    }

    @Test
    void sleeplessNights_sessionCrossingMidnight() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 0, 2, 10, 3, 0, SleepQuality.GOOD),
                session(2, 10, 23, 0, 3, 10, 7, 0, SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(sessions));
    }

    @Test
    void sleeplessNights_emptyList() {
        assertEquals("0", new SleeplessNightsFunction().apply(Collections.emptyList()));
    }

    @Test
    void sleeplessNights_sessionStartsBeforeNoon() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 2, 0, 1, 10, 5, 0, SleepQuality.GOOD),
                session(1, 10, 23, 0, 2, 10, 7, 0, SleepQuality.GOOD)
        );
        assertEquals("0", new SleeplessNightsFunction().apply(sessions));
    }

    @Test
    void sleeplessNights_crossMonth() {
        List<SleepingSession> sessions = Arrays.asList(
                session(30, 10, 23, 0, 31, 10, 7, 0, SleepQuality.GOOD),
                session(2, 11, 23, 0, 3, 11, 7, 0, SleepQuality.GOOD)
        );
        assertEquals("2", new SleeplessNightsFunction().apply(sessions));
    }

    // ChronotypeFunction

    @Test
    void chronotype_owl() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 30, 2, 10, 10, 0, SleepQuality.GOOD),
                session(2, 10, 23, 45, 3, 10, 9, 30, SleepQuality.GOOD),
                session(3, 10, 23, 15, 4, 10, 10, 30, SleepQuality.GOOD)
        );
        assertEquals("Сова", new ChronotypeFunction().apply(sessions));
    }

    @Test
    void chronotype_lark() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 21, 0, 2, 10, 6, 0, SleepQuality.GOOD),
                session(2, 10, 20, 30, 3, 10, 5, 30, SleepQuality.GOOD),
                session(3, 10, 21, 30, 4, 10, 6, 30, SleepQuality.GOOD)
        );
        assertEquals("Жаворонок", new ChronotypeFunction().apply(sessions));
    }

    @Test
    void chronotype_pigeon() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 22, 30, 2, 10, 7, 30, SleepQuality.GOOD),
                session(2, 10, 22, 45, 3, 10, 8, 0, SleepQuality.GOOD),
                session(3, 10, 23, 0, 4, 10, 8, 30, SleepQuality.GOOD)
        );
        assertEquals("Голубь", new ChronotypeFunction().apply(sessions));
    }

    @Test
    void chronotype_tieDefaultsToPigeon() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 30, 2, 10, 10, 0, SleepQuality.GOOD),
                session(2, 10, 20, 0, 3, 10, 5, 30, SleepQuality.GOOD)
        );
        assertEquals("Голубь", new ChronotypeFunction().apply(sessions));
    }

    @Test
    void chronotype_ignoresDaytimeNaps() {
        List<SleepingSession> sessions = Arrays.asList(
                session(1, 10, 23, 30, 2, 10, 10, 0, SleepQuality.GOOD),
                session(2, 10, 14, 0, 2, 10, 15, 0, SleepQuality.NORMAL),
                session(2, 10, 23, 45, 3, 10, 9, 30, SleepQuality.GOOD)
        );
        assertEquals("Сова", new ChronotypeFunction().apply(sessions));
    }
}