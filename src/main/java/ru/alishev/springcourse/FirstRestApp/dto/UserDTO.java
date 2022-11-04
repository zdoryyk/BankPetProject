package ru.alishev.springcourse.FirstRestApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import ru.alishev.springcourse.FirstRestApp.models.Transaction;
import ru.alishev.springcourse.FirstRestApp.models.User;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class UserDTO {
    @NotEmpty(message = "Название не должно быть пустым!")
    @NotNull
    private String name;

    @NotNull
    @NotEmpty(message = "Пустой пароль")
    private String password;

    private Integer balance;

    @Email(message = "should be email")
    @NotNull
    @NotEmpty
    private String email;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date birthDay;

    private String transfer;

    private int amount;

    private CardDTO cardDTO;

    private List<Transaction> transactions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CardDTO getCardDTO() {
        return cardDTO;
    }

    public void setCardDTO(CardDTO cardDTO) {
        this.cardDTO = cardDTO;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", email='" + email + '\'' +
                ", birthDay=" + birthDay +
                ", transfer='" + transfer + '\'' +
                ", amount=" + amount +
                ", cardDTO=" + cardDTO +
                ", transactions=" + transactions +
                '}';
    }
}
