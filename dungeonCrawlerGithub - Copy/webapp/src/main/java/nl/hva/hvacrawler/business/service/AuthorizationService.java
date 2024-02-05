package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import nl.hva.hvacrawler.util.security.token.JWTToken;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;
    private final JWTToken jwtToken;


    public AuthorizationService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtToken = new JWTToken();
    }

    /**
     * Controleert een JWT-token om de bijbehorende gebruiker te verifiëren.
     *
     * @param jwtTokenGiven Het JWT-token dat moet worden goedgekeurd.
     * @return De gebruiker die overeenkomt met het JWT-token als dit geldig is, anders null.
     */
    public User validateJWTToken(String jwtTokenGiven){
        User user = getUserByJWTToken(jwtTokenGiven.substring(7));
        String goodToken = jwtToken.verifyJWTToken(jwtTokenGiven, user);
        if(goodToken.equals("Valid token")){
            return user;
        }
        return null;
    }

    private User getUserByJWTToken(String jwtoken){
        return userRepository.finduserByJwtToken(jwtoken);
    }

}
