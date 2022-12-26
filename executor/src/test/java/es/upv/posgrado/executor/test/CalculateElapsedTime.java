package es.upv.posgrado.executor.test;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CalculateElapsedTime {

    LocalDateTime dt1 = LocalDateTime.parse("2022-12-26T17:59:36.998252", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    LocalDateTime dt2 = LocalDateTime.parse("2022-12-26T17:59:43.888505", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    @Test
    public void testElapsedTime() {
        Duration duration = Duration.between(dt1,dt2);
        long millis = duration.toMillis();
        String text = String.format("%02d:%02d:%02d.%03d",duration.toHoursPart(),duration.toMinutesPart(),duration.toSecondsPart(),duration.toMillisPart());
        System.out.println(text);

        long hours = ChronoUnit.HOURS.between(dt1, dt2);
        System.out.println(hours);
        long minutes = ChronoUnit.MINUTES.between(dt1, dt2);
        System.out.println(minutes);
        long seconds = ChronoUnit.SECONDS.between(dt1, dt2);
        System.out.println(seconds);
        long mills = ChronoUnit.MILLIS.between(dt1, dt2);
        System.out.println(mills);
    }
}
