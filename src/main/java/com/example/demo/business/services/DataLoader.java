package com.example.demo.business.services;

import com.example.demo.business.entities.InvalidPassword;
import com.example.demo.business.entities.Message;
import com.example.demo.business.entities.Role;
import com.example.demo.business.entities.User;
import com.example.demo.business.entities.repositories.InvalidPasswordRepository;
import com.example.demo.business.entities.repositories.MessageRepository;
import com.example.demo.business.entities.repositories.RoleRepository;
import com.example.demo.business.entities.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    InvalidPasswordRepository invalidPasswordRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        invalidPasswordRepository.save(new InvalidPassword("azerty12!"));
        invalidPasswordRepository.save(new InvalidPassword("12345678!"));
        invalidPasswordRepository.save(new InvalidPassword("password123"));

        User user = new User("jim@jim.com", "Pa$$word2019", "Jim", "Jimmerson",true,"jim");
        userService.saveUser(user);
        Message message = new Message("Mother's Day",
                "Happy mother day to the most loving mom in the world",
                LocalDate.of(2019, 05, 15),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551715335/ktlpiusvm2hecfopse7y.png",
                user);
        messageRepository.save(message);

        user = new User("mhussainshah79@gmail.com", "Pa$$word2019", "Muhammad", "Shah",true,"moe");
        userService.saveUser(user);
        message = new Message("Today is holiday",
                "Dave wants to give holiday because we did good in class",
                LocalDate.now(),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1550870732/blog/shah.jpg",
                user);
        messageRepository.save(message);

        user = new User("admin@admin.com","Pa$$word2019","Admin","User",true,"admin");
        userService.saveUser(user);
        message = new Message("Valentines Day",
                "I am still looking for someone to come in my life",
                LocalDate.of(2019, 02, 14),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1553267514/victor.png",
                user);
        messageRepository.save(message);

        user = new User("toyelani@gmail.com","Pa$$word2019","Toyelani","Obedongo",true,"toyelani");
        userService.saveUser(user);
        message = new Message("Independence Day",
                "I am proud to be an American where at least i am free. " +
                        "I wont forget men who die gave that right to me",
                LocalDate.of(2019, 07, 04),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551643804/Tolani%20Oyefule.jpg",
                user);
        messageRepository.save(message);


    }
}
