package ru.alishev.springcourse.FirstRestApp.controllers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.TransactionDTO;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.models.Transaction;
import ru.alishev.springcourse.FirstRestApp.models.User;
import ru.alishev.springcourse.FirstRestApp.services.CardService;
import ru.alishev.springcourse.FirstRestApp.services.TransactionService;
import ru.alishev.springcourse.FirstRestApp.services.UsersService;

import java.util.*;

@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final UsersService usersService;
    private final TransactionService transactionService;

    private final ModelMapper modelMapper;

    @Autowired
    public CardController(CardService cardService, UsersService usersService, TransactionService transactionService, ModelMapper modelMapper) {
        this.cardService = cardService;
        this.usersService = usersService;
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Card> getAll(){
        return cardService.getAll();
    }


    @GetMapping("/card-owner")
    public Card getInfoByCardNumber(@RequestBody Map<String,String> number){
        return cardService.getOwnerByCardNumber(new ArrayList<>(number.values()).get(0));
    }

    @GetMapping("/id-owner")
    public Card GetCardByUserId(@RequestBody Map<String,Integer> number){
        return cardService.getCardByUserId((new ArrayList<>(number.values()).get(0)));
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> addAMount(@RequestBody Map<String, Object> numbers){

        Card card = cardService.getOwnerByCardNumber((String) new ArrayList<>(numbers.values()).get(0));
        User user = usersService.getUserById(card.getUser().getId());

        user.setBalance(user.getBalance() + (Integer) new ArrayList<>(numbers.values()).get(1));
        usersService.updateUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/transfer_money")
    public ResponseEntity<HttpStatus> transfer(@RequestBody Map<String, Object> numbers){

        List<Card> cardSet = List.of(cardService.getOwnerByCardNumber((String) new ArrayList<>(numbers.values()).get(0)),
                                    cardService.getOwnerByCardNumber((String) new ArrayList<>(numbers.values()).get(1)));

        List<User> users = List.of(usersService.getUserById((cardSet.get(0).getUser().getId())),
                                                usersService.getUserById((cardSet.get(1).getUser().getId())));


        int amount = (Integer)new ArrayList<>(numbers.values()).get(2);

        System.out.println(amount);
        if(users.get(0).getBalance() > amount){
            users.get(0).setBalance(users.get(0).getBalance() - amount);
            users.get(1).setBalance(users.get(1).getBalance() + amount);
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setTransaction("send");
            transactionDTO.setUser(users.get(0));
            transactionService.save(modelMapper.map(transactionDTO, Transaction.class));
        }
        users.forEach(usersService::updateUser);

        return ResponseEntity.ok(HttpStatus.OK);
    }

//    public ResponseEntity<HttpStatus>

}
