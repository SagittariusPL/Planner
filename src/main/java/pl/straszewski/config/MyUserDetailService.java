package pl.straszewski.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.straszewski.model.UserEntity;
import pl.straszewski.repository.UserRepository;

import java.util.Optional;


@Service
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByName(name);
        if (!user.isPresent())
        {
            throw new UsernameNotFoundException("User with this nick not exist");
        }
        return new MyUserDetails(user.get());
    }
}
