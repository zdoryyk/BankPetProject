package ru.alishev.springcourse.FirstRestApp.util;

public class MyException extends RuntimeException {
    public MyException(String msg) {
        super(msg);
    }
}