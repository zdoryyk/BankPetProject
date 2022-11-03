package ru.alishev.springcourse.FirstRestApp.dto;

import ru.alishev.springcourse.FirstRestApp.models.User;

public class TransactionDTO {

    private String transaction;
    private User user;


    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                ", transaction='" + transaction + '\'' +
                ", user=" + user +
                '}';
    }
}
