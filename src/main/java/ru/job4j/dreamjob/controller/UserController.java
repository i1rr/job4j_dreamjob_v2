package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @GetMapping()
    public String getRegistrationPage() {
        return "users/registration";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model) {
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Invalid email or password");
            return "users/login";
        }
        return "redirect:/vacancies";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("message", "User with this email already exists");
            return "errors/404";
        }
        userService.save(user);
        return "redirect:/vacancies";
    }

}
