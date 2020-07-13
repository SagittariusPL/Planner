package pl.straszewski.service;

import org.springframework.stereotype.Service;
import pl.straszewski.exceptions.EmailAlreadyExistsException;
import pl.straszewski.exceptions.UserAlreadyExistsException;
import pl.straszewski.model.UserEntity;
import pl.straszewski.repository.UserRepository;

import java.util.Optional;
@Service
public class UserService {

    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkIfUserWithThisNameExist(String name) {
        Optional<UserEntity> user = userRepository.findByName(name);

        if (user.isPresent()) {
            throw new UserAlreadyExistsException("User with this name already exist");
        }
        return false;
    }

    public boolean checkIfUserWithThisEmailExist(String email) {
        Optional<UserEntity> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new EmailAlreadyExistsException("This email is already use");
        }
        return false;
    }
}

