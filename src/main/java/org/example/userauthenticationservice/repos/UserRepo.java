package org.example.userauthenticationservice.repos;

import org.example.userauthenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {


    public Optional<User> findByEmail(String email);

    public User save(User user);

}
