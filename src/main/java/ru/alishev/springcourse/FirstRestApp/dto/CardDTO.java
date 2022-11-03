package ru.alishev.springcourse.FirstRestApp.dto;

import javax.validation.constraints.NotEmpty;

public class CardDTO {

    @NotEmpty(message = "should not be empty")
    private String number;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return "CardDTO{" +
                "number='" + number + '\'' +
                '}';
    }
}
