package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.User;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 09/08/2023 15:18
 */
public interface UserDao {


    User updateUser(User user);

    User findUserByEmail(String email);

    User save(User user);

    User findUserById(int idUser);

    User findUserByjwtToken(String jwtToken);

    boolean updateResetTokenForUser(String email, String token);

    boolean updateUserIdentificatieToken(User user);
}
