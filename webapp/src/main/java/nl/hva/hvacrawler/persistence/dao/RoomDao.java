package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Room;

import java.util.Optional;


public interface RoomDao {
    void save(Room room);

    Optional<Room> getRoomById(int id);

}
