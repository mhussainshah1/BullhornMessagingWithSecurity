package com.bullhorn.business.util;

import com.bullhorn.business.entities.repositories.InvalidPasswordRepository;
import org.passay.*;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {


    @Autowired
    private InvalidPasswordRepository invalidPasswordRepository;

    private DictionaryRule dictionaryRule;

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
        for (var password : invalidPasswordRepository.findAll()) {
            passwords.add(password.getValue());
        }

        Collections.sort(passwords);

        dictionaryRule = new DictionaryRule(
                new WordListDictionary(
                        new ArrayWordList(passwords.toArray(new String[0]))));
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        var validator = new PasswordValidator(Arrays.asList(
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

        var ruleResult = validator.validate(new PasswordData(password));

        if (ruleResult.isValid()) {
            return true;
        }

        var messages = validator.getMessages(ruleResult);
        var messageTemplate = String.join(",", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

}