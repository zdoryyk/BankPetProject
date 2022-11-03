package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.repositories.CardRepository;

import java.util.List;
@Service
public class CardService {

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAll(){
        return cardRepository.findAll();
    }

    public void save(Card card){
        cardRepository.save(card);
    }

    public Card getOwnerByCardNumber(String number){
        return cardRepository.getCardByCardNumber(number);
    }
    public Card getCardByUserId(int id){
        return cardRepository.getCardByUserId(id);
    }
}
