package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findUserByUsername(String username);
}
