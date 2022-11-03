package ru.alishev.springcourse.FirstRestApp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cascade;
import ru.alishev.springcourse.FirstRestApp.util.CreditCardNumberGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Random;

@Entity
@Table(name = "cards")
public class Card implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "card_number")
    private String cardNumber;

    @OneToOne()
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;


    public Card() {
    }

    public Card(String cardNumber){
        this.cardNumber = cardNumber;
    }

    public String generateCard(){
        return new CreditCardNumberGenerator().generate("4149",16);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", user=" + user +
                '}';
    }
}
