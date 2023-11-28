package com.example.passpringrest.dto;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbDateFormat;

import java.util.GregorianCalendar;

public class DateDto {

    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
    private GregorianCalendar date;

    public DateDto() {
    }

    @JsonbCreator
    public DateDto(@JsonbProperty("rentEndDate") GregorianCalendar date) {
        this.date = date;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }
}
