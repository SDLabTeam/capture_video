package com.uploader;


import com.uploader.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTest {

    @Test
    public void shouldReturnCurrentFolder() {
        Assert.assertEquals("2021-01", DateTime.getCurrentFolder(LocalDateTime.parse("2021-01-29T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Assert.assertEquals("2021-02", DateTime.getCurrentFolder(LocalDateTime.parse("2021-02-10T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Assert.assertEquals("2021-07", DateTime.getCurrentFolder(LocalDateTime.parse("2021-07-29T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Assert.assertEquals("2021-10", DateTime.getCurrentFolder(LocalDateTime.parse("2021-10-29T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Assert.assertEquals("2021-12", DateTime.getCurrentFolder(LocalDateTime.parse("2021-12-29T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        Assert.assertEquals("2022-01", DateTime.getCurrentFolder(LocalDateTime.parse("2022-01-29T16:01:33", DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

    }
}