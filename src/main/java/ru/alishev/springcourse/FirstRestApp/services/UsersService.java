package ru.alishev.springcourse.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alishev.springcourse.FirstRestApp.models.User;
import ru.alishev.springcourse.FirstRestApp.repositories.CardRepository;
import ru.alishev.springcourse.FirstRestApp.repositories.TransactionRepository;
import ru.alishev.springcourse.FirstRestApp.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    private final UserRepository userRepository;

    private final CardRepository cardRepository;

    private final TransactionRepository transactionRepository;

    @Autowired
    public UsersService(UserRepository userRepository, CardRepository cardRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.getById(id);
    }

    public User getUserByName(String name){return userRepository.getUserByName(name);}
    public void register(User user){
        user.setCreated_at(LocalDateTime.now());
        userRepository.save(user);
    }
    public void updateUser(User user){
        userRepository.save(user);
    }

    public void deleteUser(User user){
        cardRepository.delete(user.getCard());
        userRepository.delete(user);
    }

}
