package com.example.demo.business.services;

import com.example.demo.business.entities.InvalidPassword;
import com.example.demo.business.entities.repositories.InvalidPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PasswordService {

    @Autowired
    private InvalidPasswordRepository invalidPasswordRepository;

    private List<String> passwords;

    public PasswordService(){
        passwords = new ArrayList<>();
    }

    public List<String> getPasswords() {
        //        invalidPasswordRepository.findAll().forEach(p -> passwords.add(p.getValue()));
        for (InvalidPassword password : invalidPasswordRepository.findAll()) {
            System.out.println("invalid password = " + password.getValue());
            passwords.add(password.getValue());
        }

        return passwords;
    }
}
