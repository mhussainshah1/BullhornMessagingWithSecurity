package com.example.demo.business.util;
//todo: change reading from datatbase rather than text file reference is on
// http://www.passay.org/reference/

import com.example.demo.business.entities.InvalidPassword;
import com.example.demo.business.entities.repositories.InvalidPasswordRepository;
import com.example.demo.business.services.PasswordService;
import org.passay.*;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {


    private DictionaryRule dictionaryRule;

    @Autowired
    PasswordService passwordService;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {

        //Option 1 : Through File
        /*try {
            String invalidPasswordList = this.getClass().getResource("/invalid-password-list.txt").getFile();
            dictionaryRule = new DictionaryRule(
                    new WordListDictionary(WordLists.createFromReader(
                            // Reader around the word list file
                            new FileReader[]{
                                    new FileReader(invalidPasswordList)
                            },
                            // True for case sensitivity, false otherwise
                            false,
                            // Dictionaries must be sorted
                            new ArraysSort()
                    )));
        } catch (IOException e) {
            throw new RuntimeException("could not load word list", e);
        }*/

        //Option 2 : Through Database
        List<String> passwords = new ArrayList<>();
        //passwordService.getPasswords();

        passwords.add("azerty12!");
        passwords.add("12345678!");
        passwords.add("password123");
        Collections.sort(passwords);

        dictionaryRule = new DictionaryRule(
                new WordListDictionary(
                        new ArrayWordList(passwords.stream().toArray(String[]::new))));
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                // at least 8 characters
                new LengthRule(8, 60),

                // at least one upper-case character
                new CharacterRule(EnglishCharacterData.UpperCase, 1),

                // at least one lower-case character
                new CharacterRule(EnglishCharacterData.LowerCase, 1),

                // at least one digit character
                new CharacterRule(EnglishCharacterData.Digit, 1),

                // at least one symbol (special character)
                new CharacterRule(EnglishCharacterData.Special, 1),

                // no whitespace
                new WhitespaceRule(),

                // no common passwords
                dictionaryRule
        ));

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

}