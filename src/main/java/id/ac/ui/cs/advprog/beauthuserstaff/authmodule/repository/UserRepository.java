package id.ac.ui.cs.advprog.beauthuserstaff.authmodule.repository;

import id.ac.ui.cs.advprog.beauthuserstaff.authmodule.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
