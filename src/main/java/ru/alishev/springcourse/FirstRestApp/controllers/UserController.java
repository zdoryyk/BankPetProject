package ru.alishev.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.UserDTO;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.models.User;
import ru.alishev.springcourse.FirstRestApp.services.CardService;
import ru.alishev.springcourse.FirstRestApp.services.UsersService;
import ru.alishev.springcourse.FirstRestApp.util.Response;
import ru.alishev.springcourse.FirstRestApp.util.UserValidator;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
@Transactional(readOnly = true)
public class UserController {

    private final UsersService usersService;
    private final CardService cardService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    @Autowired
    public UserController(UsersService service, CardService cardService, ModelMapper modelMapper, UserValidator userValidator) {
        this.usersService = service;
        this.cardService = cardService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping
    public List<User> GetAllUsers(){
        return usersService.getAll();
    }


    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<?> deleteUser(@RequestBody UserDTO userDTO){

        User userToDelete = convertToUser(userDTO);
        User checkUser = usersService.getUserByEmail(userDTO.getEmail());

        if(checkUser == null)
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"User not found"));

        if(userToDelete.getPassword().equals(checkUser.getPassword())) {
            usersService.deleteUser(checkUser);
            return ResponseEntity.ok(HttpStatus.OK);
        }else
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"Passwords are not the same"));

    }



    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            StringBuilder errors = new StringBuilder();
            List<FieldError> errorList = bindingResult.getFieldErrors();
            for(FieldError error: errorList)
                errors.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append("; ");

            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_GATEWAY,errors.toString()));
        }

        Card card = new Card();
        String cardNumber = card.generateCard();
        while (cardService.isEmpty(cardNumber))
            cardNumber = card.generateCard();

        card.setCardNumber(cardNumber);
        User user = convertToUser(userDTO);
        user.setCard(card);
        card.setUser(user);

        cardService.save(card);
        usersService.register(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user")
    public Object getUser(@RequestBody UserDTO user){

        if(user.getEmail() == null || user.getEmail().isEmpty())
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"Empty fields"));

        if(usersService.getUserByEmail(user.getEmail()) == null)
            return ResponseEntity.badRequest().body(new Response(HttpStatus.BAD_REQUEST,"User not found"));

        return usersService.getUserByEmail(user.getEmail());
    }

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}

