package io.github.cres95.rs2world.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Frequency is an extension of Duration which simply adds a value representing the amount of times an event occurs
 * within the given Duration. The notation is x/d where x is a positive integer representing the occurrences and d is
 * the duration of time, expressed in any valid Java Duration notation.
 */
public class Frequency {

    private static final Pattern REGEX_PATTERN = Pattern.compile("^([1-9]\\d*)/(.+)$");

    private final long occurrences;
    private final Duration duration;

    private Frequency(long occurences, Duration duration) {
        this.occurrences = occurences;
        this.duration = duration;
    }

    public static Frequency of(String text) {
        Matcher matcher = REGEX_PATTERN.matcher(text);
        try {
            if (matcher.find()) {
                long occurrences = Long.parseLong(matcher.group(1));
                Duration duration = Duration.parse(matcher.group(2));
                return new Frequency(occurrences, duration);
            } else {
                throw new IllegalArgumentException("'%s' is not a valid Frequency format".formatted(text));
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("'%s' is not a valid Frequency format".formatted(text), e);
        }
    }

    public long getOccurrences() {
        return occurrences;
    }

    public Duration getDuration() {
        return duration;
    }
}
