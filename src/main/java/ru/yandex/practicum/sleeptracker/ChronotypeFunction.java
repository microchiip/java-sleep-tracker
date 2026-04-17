package ru.yandex.practicum.sleeptracker;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChronotypeFunction implements AnalyticsFunction {

    enum Chronotype {
        OWL("Owl"),
        LARK("Lark"),
        PIGEON("Pigeon");

        private final String displayName;

        Chronotype(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Override
    public String getName() {
        return "Chronotype";
    }

    @Override
    public String apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(this::isNightSession)
                .map(this::classifySession)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (owlCount > larkCount && owlCount > pigeonCount) {
            return Chronotype.OWL.getDisplayName();
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            return Chronotype.LARK.getDisplayName();
        } else {
            return Chronotype.PIGEON.getDisplayName();
        }
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime fallAsleepTime = session.getFallAsleepTime().toLocalTime();
        return !fallAsleepTime.isBefore(LocalTime.of(18, 0)) || fallAsleepTime.isBefore(LocalTime.of(6, 0));
    }

    Chronotype classifySession(SleepingSession session) {
        LocalTime fallAsleepTime = session.getFallAsleepTime().toLocalTime();
        LocalTime wakeUpTime = session.getWakeUpTime().toLocalTime();

        boolean isOwlSleep = fallAsleepTime.isAfter(LocalTime.of(23, 0));
        boolean isOwlWake = wakeUpTime.isAfter(LocalTime.of(9, 0));

        boolean isLarkSleep = fallAsleepTime.isBefore(LocalTime.of(22, 0))
                && !fallAsleepTime.isBefore(LocalTime.of(6, 0));
        boolean isLarkWake = wakeUpTime.isBefore(LocalTime.of(7, 0));

        if (isOwlSleep && isOwlWake) {
            return Chronotype.OWL;
        } else if (isLarkSleep && isLarkWake) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }
}
