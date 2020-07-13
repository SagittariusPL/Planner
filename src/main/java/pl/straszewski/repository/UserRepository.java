package pl.straszewski.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.straszewski.model.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);
    Optional<UserEntity> findByEmail(String email);


}
