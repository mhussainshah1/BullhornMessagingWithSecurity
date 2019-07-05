package com.example.demo.web.controller;

import com.cloudinary.utils.ObjectUtils;
import com.example.demo.business.entities.Message;
import com.example.demo.business.entities.User;
import com.example.demo.business.entities.repositories.MessageRepository;
import com.example.demo.business.entities.repositories.UserRepository;
import com.example.demo.business.services.CloudinaryConfig;
import com.example.demo.business.services.CustomerUserDetails;
import com.example.demo.business.services.UserService;
import com.example.demo.business.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
        return "admin";
    }

    @RequestMapping("/")
    public String listMessages(Model model) {
        model.addAttribute("messages", messageRepository.findAll());//generate select * statement
        //we need because the below statement wont run if there is no authenticate user
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
            model.addAttribute("user", userService.getUser());
//            model.addAttribute("HASH", MD5Util.md5Hex(userService.getUser().getEmail()));
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
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e);
            }
            return "messageform";
        }

        if (!file.isEmpty()) {
            try {
                Map uploadResult = cloudc.upload(
                        file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
                String url = uploadResult.get("url").toString();
                String uploadedName = uploadResult.get("public_id").toString();
                String transformedImage = cloudc.createUrl(uploadedName, 150, 150);
                message.setPicturePath(transformedImage);
                message.setUser(userService.getUser());
            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/add";
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
            model.addAttribute("user", userService.getUser());
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

    @GetMapping("/about")
    public String getAbout() {
        return "about";
    }

    @RequestMapping("/myprofile")
    public String getProfile(Principal principal, Model model) {
        User user = userService.getUser();
        model.addAttribute("user", user);
        model.addAttribute("myuser", user);
        model.addAttribute("HASH", MD5Util.md5Hex(user.getEmail()));
        return "profile";
    }

    @RequestMapping("/user/{id}")
    public String getUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).get();
        model.addAttribute("user", user);
        model.addAttribute("myuser", userService.getUser());
        model.addAttribute("HASH", MD5Util.md5Hex(user.getEmail())); //save every person email as hash
        return "profile";
    }

    @RequestMapping("/followers")
    public String getFollowers(Model model) {
        model.addAttribute("message", "My Followers");
        model.addAttribute("md5Util", new MD5Util());
        model.addAttribute("users", userRepository.findAllByFollowings(userService.getUser()));
        return "peoplelist";
    }

    @RequestMapping("/following")
    public String getFollowing(Model model) {
        model.addAttribute("message", "People I`m Following");
        model.addAttribute("md5Util", new MD5Util());
        model.addAttribute("users", userRepository.findAllByFollowers(userService.getUser()));
        return "peoplelist";
    }

    @RequestMapping("/follow/{id}")
    public String follow(@PathVariable("id") long id, Model model) {
        User follow = userRepository.findById(id).get();
        User myuser = userService.getUser();
        myuser.addFollowing(follow);
        userRepository.save(myuser);
        return "redirect:/";
    }

    @RequestMapping("/unfollow/{id}")
    public String unfollow(@PathVariable("id") long id, Model model) {
        User follow = userRepository.findById(id).get();
        User myuser = userService.getUser();
        myuser.removeFollowing(follow);
        userRepository.save(myuser);
        return "redirect:/";
    }

    //AUXILLARY FUNCTION!!!
    //Use the below code INSIDE METHOD to pass user into view
    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {

        User myuser = ((CustomerUserDetails)
                ((UsernamePasswordAuthenticationToken) principal)
                        .getPrincipal())
                .getUser();
        //or
        myuser = userService.getUser();
        model.addAttribute("myuser", myuser);
        return "secure";
    }
}
