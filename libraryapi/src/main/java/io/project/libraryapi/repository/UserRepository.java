package io.project.libraryapi.repository;

import io.project.libraryapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findByLogin(String Login);

    User findByEmail(String email);
}
