package com.example.demo.business.services;

import com.example.demo.business.entities.Course;
import com.example.demo.business.entities.Message;
import com.example.demo.business.entities.Role;
import com.example.demo.business.entities.User;
import com.example.demo.business.entities.repositories.CourseRepository;
import com.example.demo.business.entities.repositories.MessageRepository;
import com.example.demo.business.entities.repositories.RoleRepository;
import com.example.demo.business.entities.repositories.UserRepository;
import com.example.demo.business.services.UserService;
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
    MessageRepository repository;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole=roleRepository.findByRole("USER");

        User user = new User("jim@jim.com", "Pa$$word2019", "jim", "jimmerson",true,"jim");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        user = new User("admin@admin.com","Pa$$word2019","Admin","User",true,"admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        Message message = new Message("Today is holiday",
                "Dave wants to give holiday because we did good in class",
                LocalDate.now(),
                "Muhammad",
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1550870732/blog/shah.jpg");
        message.setUser(user);
        repository.save(message);

        message = new Message("Valentines Day",
                "I am still looking for someone to come in my life",
                LocalDate.of(2019, 02, 14),
                "Victor",
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1553267514/victor.png");
        message.setUser(user);
        repository.save(message);

        message = new Message("Independence Day",
                "I am proud to be an American where at least i am free. " +
                        "I wont forget men who die gave that right to me",
                LocalDate.of(2019, 07, 04),
                "Toyelani",
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551643804/Tolani%20Oyefule.jpg");
        message.setUser(user);
        repository.save(message);

        message = new Message("Mother's Day",
                "Happy mother day to the most loving mom in the world",
                LocalDate.of(2019, 05, 15),
                "Melisa",
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551715335/ktlpiusvm2hecfopse7y.png");
        message.setUser(user);
        repository.save(message);
    }
}
