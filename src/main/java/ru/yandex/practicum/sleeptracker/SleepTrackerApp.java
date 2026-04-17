package ru.yandex.practicum.sleeptracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<AnalyticsFunction> functions = Arrays.asList(
            new TotalSessionsFunction(),
            new MinDurationFunction(),
            new MaxDurationFunction(),
            new AverageDurationFunction(),
            new BadQualityCountFunction(),
            new SleeplessNightsFunction(),
            new ChronotypeFunction()
    );

    public static List<SleepingSession> readSleepLog(String filePath) throws IOException {
        return Files.lines(Paths.get(filePath))
                .map(String::trim)
                .filter(line -> !line.isEmpty())
                .map(SleepTrackerApp::parseLine)
                .collect(Collectors.toList());
    }

    private static SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        LocalDateTime fallAsleep = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime wakeUp = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim());
        return new SleepingSession(fallAsleep, wakeUp, quality);
    }

    public List<AnalyticsFunction> getFunctions() {
        return functions;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java SleepTrackerApp <path-to-sleep-log>");
            return;
        }

        try {
            List<SleepingSession> sessions = readSleepLog(args[0]);
            SleepTrackerApp app = new SleepTrackerApp();
            app.getFunctions().stream()
                    .map(f -> f.getName() + ": " + f.apply(sessions))
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
