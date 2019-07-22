package com.bullhorn.business.services;

import com.bullhorn.business.entities.InvalidPassword;
import com.bullhorn.business.entities.Message;
import com.bullhorn.business.entities.Role;
import com.bullhorn.business.entities.User;
import com.bullhorn.business.entities.repositories.InvalidPasswordRepository;
import com.bullhorn.business.entities.repositories.MessageRepository;
import com.bullhorn.business.entities.repositories.RoleRepository;
import com.bullhorn.business.entities.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    InvalidPasswordRepository invalidPasswordRepository;

    @Override
    public void run(String... args) throws Exception {
        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");

        invalidPasswordRepository.save(new InvalidPassword("azerty12!"));
        invalidPasswordRepository.save(new InvalidPassword("12345678!"));
        invalidPasswordRepository.save(new InvalidPassword("password123"));

        User dave = new User("dave45678@gmail.com",
                userService.encode("password"),
                "David",
                "Wolf",
                true,
                "dave");
        userService.saveUser(dave);

        User moe = new User("mhussainshah79@gmail.com",
                userService.encode("password"),
                "Muhammad",
                "Shah",
                true,
                "moe");
        userService.saveUser(moe);

        User tolani = new User("xdwr@my.qsl.ro",
                userService.encode("password"),
                "Tolani",
                "Oyefule",
                true,
                "lan");
        userService.saveUser(tolani);

        User admin = new User("study.javaclass@gmail.com",
                userService.encode("password"),
                "Admin",
                "User",
                true,
                "admin");
        userService.saveAdmin(admin);

        Message message = new Message("Mother's Day",
                "Happy mother day to the most loving mom in the world",
                LocalDateTime.of(2019, 05, 15, 14, 15),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551323218/java-bootcamp/roohi_bano.jpg",
                dave);
        messageRepository.save(message);

        message = new Message("Today is holiday",
                "Dave wants to give holiday because we did good in class",
                LocalDateTime.now(),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551473204/java-bootcamp/completed.png",
                moe);
        messageRepository.save(message);

        message = new Message("Independence Day",
                "I am proud to be an American where at least i am free. " +
                        "I wont forget men who die gave that right to me",
                LocalDateTime.of(2019, 07, 04, 10, 11),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1563037288/java-bootcamp/american-flag-internal-halyard.jpg",
                tolani);
        messageRepository.save(message);

        message = new Message("Valentines Day",
                "I am still looking for someone to come in my life",
                LocalDateTime.of(2019, 02, 14, 05, 06),
                "https://res.cloudinary.com/mhussainshah1/image/upload/v1551323276/java-bootcamp/hebapsmsapt323cll6ak.jpg",
                admin);
        messageRepository.save(message);

        //Add followers
        dave.addFollower(admin);
        userRepository.save(admin);

        //Add Following
        dave.addFollowing(moe);
        dave.addFollowing(tolani);
        userRepository.save(dave);
    }
}
