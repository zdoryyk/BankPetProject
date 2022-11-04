package ru.alishev.springcourse.FirstRestApp.controllers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.CardDTO;
import ru.alishev.springcourse.FirstRestApp.dto.TransactionDTO;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.models.Transaction;
import ru.alishev.springcourse.FirstRestApp.models.TransferMoney;
import ru.alishev.springcourse.FirstRestApp.models.User;
import ru.alishev.springcourse.FirstRestApp.services.CardService;
import ru.alishev.springcourse.FirstRestApp.services.TransactionService;
import ru.alishev.springcourse.FirstRestApp.services.UsersService;
import ru.alishev.springcourse.FirstRestApp.util.Response;

import java.util.*;

@RestController
@RequestMapping("/cards")
@Transactional(readOnly = true)
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

    @PostMapping("/transfer-money")
    @Transactional
    public ResponseEntity<?> transfer(@RequestBody TransferMoney transferMoney){

        if(cardService.getOwnerByCardNumber(transferMoney.getSender()) == null)
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"Sender not found"));
        if(cardService.getOwnerByCardNumber(transferMoney.getRecipient()) == null)
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"Recipient not found"));

        User sender = cardService.getOwnerByCardNumber(transferMoney.getSender()).getUser();
        User recipient = cardService.getOwnerByCardNumber(transferMoney.getRecipient()).getUser();

        if(sender.getBalance() < transferMoney.getAmount())
                return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"Not enough money"));


        sender.setBalance(sender.getBalance() - transferMoney.getAmount());
        recipient.setBalance(recipient.getBalance() + transferMoney.getAmount());
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUser(sender);
        transactionDTO.setTransaction("send");
        transactionService.save(modelMapper.map(transactionDTO, Transaction.class));
        usersService.updateUser(List.of(sender,recipient));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> addAMount(@RequestBody TransferMoney transferMoney){

        if(cardService.getOwnerByCardNumber(transferMoney.getRecipient()) == null)
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"User not found"));

        User recipient = cardService.getOwnerByCardNumber(transferMoney.getRecipient()).getUser();

        recipient.setBalance(recipient.getBalance() + transferMoney.getAmount());

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setUser(recipient);
        transactionDTO.setTransaction("put");
        transactionService.save(modelMapper.map(transactionDTO, Transaction.class));
        usersService.updateUser(recipient);

        return ResponseEntity.ok(HttpStatus.OK);
    }


 }
