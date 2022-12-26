package es.upv.posgrado.connectors.newsdata.test;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConvertDateTest {
    String pubDate = "2022-02-04 07:03:47";

    @Test
    public void convertPubDateToLocalDateTimeTest(){
        LocalDateTime parse = LocalDateTime.parse(pubDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(parse);
    }
}
