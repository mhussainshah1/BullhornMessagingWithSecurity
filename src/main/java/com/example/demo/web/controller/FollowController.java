package com.example.demo.web.controller;

import com.example.demo.business.entities.User;
import com.example.demo.business.entities.repositories.UserRepository;
import com.example.demo.business.services.UserService;
import com.example.demo.business.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FollowController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

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
}
