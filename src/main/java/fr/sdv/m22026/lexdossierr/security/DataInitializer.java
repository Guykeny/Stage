package fr.sdv.m22026.lexdossierr.security;

import fr.sdv.m22026.lexdossierr.entity.Role;
import fr.sdv.m22026.lexdossierr.entity.User;
import fr.sdv.m22026.lexdossierr.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepository.findByEmail("avocat@lex.fr").isEmpty()){
                User user = new User();
                user.setEmail("avocat@lex.fr");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRole(Role.AVOCAT);
                userRepository.save(user);
            }
            if(userRepository.findByEmail("assistant@lex.fr").isEmpty()){
                User user = new User();
                user.setEmail("assistant@lex.fr");
                user.setPassword(passwordEncoder.encode("avocat123"));
                user.setRole(Role.ASSISTANT);
                userRepository.save(user);
            }
        };
    }
}
