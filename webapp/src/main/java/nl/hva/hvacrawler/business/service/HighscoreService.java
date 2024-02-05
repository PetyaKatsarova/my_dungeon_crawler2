package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HighscoreService {
    private final UserRepository userRepository;

    public HighscoreService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Highscore> getHighScoreslist(int limit) {
        return userRepository.getHighScoreslist(limit);
    }

    public List<Highscore> getHighScoresAroundName(String name) {
        return userRepository.getHighScoresAroundName(name);
    }
    public List<Highscore> getHighScoresAroundRank(int rank) {
        return userRepository.getHighScoresAroundRank(rank);
    }
}
