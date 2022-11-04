package ru.alishev.springcourse.FirstRestApp.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.TransactionDTO;
import ru.alishev.springcourse.FirstRestApp.models.Transaction;
import ru.alishev.springcourse.FirstRestApp.services.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final ModelMapper modelMapper;

    @Autowired
    public TransactionController(TransactionService transactionService, ModelMapper modelMapper) {
        this.transactionService = transactionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Transaction> getAll(){
        return transactionService.getAll();
    }


    private Transaction convertToTransaction(TransactionDTO transactionDTO){
        return modelMapper.map(transactionDTO, Transaction.class);
    }
    private TransactionDTO convertToTransactionDTO(Transaction transaction){
        return modelMapper.map(transaction, TransactionDTO.class);
    }

}
