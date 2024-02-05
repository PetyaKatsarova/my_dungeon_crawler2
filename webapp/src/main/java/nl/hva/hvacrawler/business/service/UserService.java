package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

}

