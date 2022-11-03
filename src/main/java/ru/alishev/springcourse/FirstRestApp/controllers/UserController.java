package ru.alishev.springcourse.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alishev.springcourse.FirstRestApp.dto.UserDTO;
import ru.alishev.springcourse.FirstRestApp.models.Card;
import ru.alishev.springcourse.FirstRestApp.models.User;
import ru.alishev.springcourse.FirstRestApp.services.CardService;
import ru.alishev.springcourse.FirstRestApp.services.UsersService;
import ru.alishev.springcourse.FirstRestApp.util.UserValidator;

import javax.validation.Valid;
import java.util.*;

import static ru.alishev.springcourse.FirstRestApp.util.ErrorsUtil.returnErrorsToClient;

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
    public ResponseEntity<HttpStatus> deleteUser(@RequestBody Map<String,Object> userToDelete){

        List<String> keys = new ArrayList<>(userToDelete.keySet());
        List<Object> values = new ArrayList<>(userToDelete.values());

        if(keys.get(0).equals("name") && keys.get(1).equals("password")
                        || keys.get(0).equals("password") && keys.get(1).equals("name") ){
            if(usersService.getUserByName( (String) values.get(0) ) == null)
                return ResponseEntity.ok(HttpStatus.NOT_FOUND);

            User user = usersService.getUserByName( (String) values.get(0) );

            if(user.getPassword().equals(values.get(1))){
                usersService.deleteUser(user);
                return ResponseEntity.ok(HttpStatus.OK);
            }
        }
        return ResponseEntity.ok(HttpStatus.CONFLICT);
    }


    @PostMapping("/register")
    @Transactional
    public ResponseEntity<Object> addUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult){

        userValidator.validate(convertToUser(userDTO),bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.ok(HttpStatus.CONFLICT);
//            returnErrorsToClient(bindingResult);
        }

        Card card = new Card();
        card.setCardNumber(card.generateCard());
        User user = convertToUser(userDTO);
        user.setCard(card);
        card.setUser(user);

        cardService.save(card);
        usersService.register(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/user")
    public User getUser(@RequestBody Map<String,String> name){
        return usersService.getUserByName(new ArrayList<>(name.values()).get(0));
    }

    private User convertToUser(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }
    private UserDTO convertToUserDTO(User user){
        return modelMapper.map(user, UserDTO.class);
    }
}

