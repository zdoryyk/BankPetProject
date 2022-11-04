package ru.alishev.springcourse.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.models.User;

import java.util.List;

public interface CardRepository extends JpaRepository<Card,Integer> {

    Card getCardByCardNumber(String number);
    Card getCardByUserId(int id);

    @Modifying
    @Query(value = "select card_number from cards ",nativeQuery = true)
    List<String> getAllCardNumbers();

}
