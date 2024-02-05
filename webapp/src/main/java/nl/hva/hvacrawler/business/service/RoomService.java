package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Room;
import nl.hva.hvacrawler.persistence.repository.GameRepository;
import nl.hva.hvacrawler.persistence.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Optional<Room> getRoomById(int id) {
        return roomRepository.getRoomById(id);
    }

    public void saveOrUpdateRoom(Room room) {
        roomRepository.saveOrUpdateOne(room);
    }
}
