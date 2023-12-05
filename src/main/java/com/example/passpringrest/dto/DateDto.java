package com.example.passpringrest.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.GregorianCalendar;

public class DateDto {

    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private GregorianCalendar date;

    public DateDto() {
    }

    @JsonCreator
    public DateDto(@JsonProperty("rentEndDate") GregorianCalendar date) {
        this.date = date;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
