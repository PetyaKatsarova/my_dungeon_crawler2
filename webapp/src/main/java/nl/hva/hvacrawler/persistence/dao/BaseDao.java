package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Crawler;

import java.util.Optional;

/**
 * Description: Standardize common methods to interact with db
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 23/08/2023 12:34
 */
public interface BaseDao<T> {
    T saveOrUpdateOne(T obj); // generic type T, replace with your class
    Optional<T> findOneById(int id);
}
