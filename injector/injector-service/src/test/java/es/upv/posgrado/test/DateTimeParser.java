package es.upv.posgrado.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeParser {

    String newsApiDate = "2022-12-11T01:33:44Z";
    String mediaStackDate = "2020-07-17T23:35:06+00:00";

    String rssQuarkusFeedDate = "Wed, 21 Dec 2022 00:00:00 +0000";

    @Test
    public void testParserDate() {
        LocalDateTime dt1 = LocalDateTime.parse(newsApiDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        LocalDateTime dt2 = LocalDateTime.parse(mediaStackDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        LocalDateTime dt3 = OffsetDateTime.parse(rssQuarkusFeedDate, DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z")).toLocalDateTime();
    }

    @Test
    public void generateDateInFormat() {
        OffsetDateTime now = OffsetDateTime.now();
        System.out.println(now.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z")));
    }
}
