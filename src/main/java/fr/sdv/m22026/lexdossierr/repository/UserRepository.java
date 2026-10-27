package fr.sdv.m22026.lexdossierr.repository;


import fr.sdv.m22026.lexdossierr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
