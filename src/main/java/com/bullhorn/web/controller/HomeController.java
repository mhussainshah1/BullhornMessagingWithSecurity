package com.bullhorn.web.controller;

import com.bullhorn.business.entities.Message;
import com.bullhorn.business.entities.repositories.MessageRepository;
import com.bullhorn.business.entities.repositories.UserRepository;
import com.bullhorn.business.services.CloudinaryConfig;
import com.bullhorn.business.services.CustomerUserDetails;
import com.bullhorn.business.services.UserService;
import com.bullhorn.business.util.MD5Util;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    MessageRepository messageRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @Autowired
    UserService userService;

    //Users with Admin role can view this page
    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("mD5Util", new MD5Util());
        return "admin";
    }

    @RequestMapping("/")
    public String listMessages(Model model) {
        model.addAttribute("messages", messageRepository.findAllByOrderByPostedDateTimeDesc());//generate select * statement
        //we need because the below statement wont run if there is no authenticate user
        if (userService.getUser() != null) {
            model.addAttribute("user", userService.getUser());
        }
        model.addAttribute("mD5Util", new MD5Util());
        return "list";
    }

    @GetMapping("/add")
    public String messageForm(Principal principal, Model model) {
        model.addAttribute("imageLabel", "Upload Image");
        model.addAttribute("user", userService.getUser());
        model.addAttribute("message", new Message());
        return "messageform";
    }

    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute("message") Message message,
                              BindingResult result,
                              @RequestParam("file") MultipartFile file,
                              Model model) {
        model.addAttribute("imageLabel", "Upload Image");
        model.addAttribute("user", userService.getUser());
        //check for errors on the form
        if (result.hasErrors()) {
        /*  for (var objectError : result.getAllErrors()) {
                System.out.println(objectError);
            }*/
            return "messageform";
        }

        if (!file.isEmpty()) {
            try {
                Map uploadResult = cloudc.upload(
                        file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                var url = uploadResult.get("url").toString();
                var uploadedName = uploadResult.get("public_id").toString();
                var transformedImage = cloudc.createUrl(uploadedName, 150, 150);
                message.setPicturePath(transformedImage);
                message.setUser(userService.getUser());
            } catch (IOException e) {
                e.printStackTrace();
//                return "redirect:messageform";
            }
        } else {
            //if file is empty and there is a picture path then save item
            if (message.getPicturePath().isEmpty()) {
                return "messageform";
            }
        }
        messageRepository.save(message);
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateMessage(@PathVariable("id") long id, Model model) {
        model.addAttribute("message", messageRepository.findById(id).get());
        if (userService.getUser() != null) {
            model.addAttribute("user", userService.getUser());
        }
        return "messageform";
    }

    @RequestMapping("/delete/{id}")
    public String deleteMessage(@PathVariable("id") long id) {
        messageRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/check")
    public String check(@RequestParam("check") long[] ids,
                        Model model) {
        for (var id : ids) {
            messageRepository.deleteById(id);
        }
        return "redirect:/";
    }

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @RequestMapping("/myprofile")
    public String getProfile(Principal principal, Model model) {
        var user = userService.getUser();
        model.addAttribute("user", user);
        model.addAttribute("myuser", user);
        model.addAttribute("HASH", MD5Util.md5Hex(user.getEmail()));
        return "profile";
    }

    @RequestMapping("/user/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        var user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("myuser", userService.getUser());
        model.addAttribute("HASH", MD5Util.md5Hex(user.getEmail())); //save every person email as hash
        return "profile";
    }

    //AUXILLARY FUNCTION!!!
    //Use the below code INSIDE METHOD to pass user into view
    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {

        var myuser = ((CustomerUserDetails)
                ((UsernamePasswordAuthenticationToken) principal)
                        .getPrincipal())
                .getUser();
        //or
        myuser = userService.getUser();
        model.addAttribute("myuser", myuser);
        return "secure";
    }
}
