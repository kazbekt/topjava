package ru.javawebinar.topjava.web.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public LocalTime convert(String timeString) {
        return (timeString == null || timeString.trim().isEmpty()) ? null : LocalTime.parse(timeString, timeFormatter);
    }
}
