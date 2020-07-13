package pl.straszewski.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.straszewski.DTOs.UserDTO;
import pl.straszewski.exceptions.EmailAlreadyExistsException;
import pl.straszewski.exceptions.IncorrectDataException;
import pl.straszewski.exceptions.UserAlreadyExistsException;
import pl.straszewski.model.Token;
import pl.straszewski.model.UserEntity;
import pl.straszewski.repository.UserRepository;
import pl.straszewski.service.TokenService;
import pl.straszewski.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private UserService userService;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                                  TokenService tokenService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.userService = userService;
    }


    @ModelAttribute("user")
    public UserDTO userDTO() {
        return new UserDTO();
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        return "registration";
    }


    @PostMapping("/registration")
    public String registrationPage(@ModelAttribute("user") UserDTO userDTO, Model model, HttpServletRequest request ) {
        try {
            if (userDTO.isValid() && !userService.checkIfUserWithThisNameExist(userDTO.getName())
                    && !userService.checkIfUserWithThisEmailExist(userDTO.getEmail())) {
                UserEntity user = new UserEntity(userDTO.getName(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getEmail());
                userRepository.save(user);
                tokenService.sendToken(user,request);
                model.addAttribute("status", "To confirm your account please visit your mailbox");
            }
        } catch (IncorrectDataException e) {

            model.addAttribute("exceptions", e.getErrors());
            return "registration";
        } catch (UserAlreadyExistsException | EmailAlreadyExistsException e) {
            model.addAttribute("status", e.getMessage());
            return "registration";
        }
        return "registration";
    }


    @GetMapping(value = "/registration/token", params = "value")
    private String confirmToken(@RequestParam("value") String value, Model model) {
        Token token = tokenService.findToken(value);

        UserEntity user = token.getUserEntity();
        user.setEnabled(true);
        userRepository.save(user);
        model.addAttribute("activate", "Your account is activated");
        return "/activated";
    }
}
