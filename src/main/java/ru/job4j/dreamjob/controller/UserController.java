package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.dreamjob.model.User;
import ru.job4j.dreamjob.service.SimpleUserService;
import ru.job4j.dreamjob.service.UserService;

@ThreadSafe
@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(SimpleUserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getRegistrationPage() {
        return "users/registration";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/vacancies";
    }

}
