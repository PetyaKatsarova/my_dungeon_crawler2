package nl.hva.hvacrawler.persistence.repository;

/**
 * Description: Tests for RoomRepository
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 11/09/2023 16:40
 */
import nl.hva.hvacrawler.business.domain.Chest;
import nl.hva.hvacrawler.business.domain.Room;
import nl.hva.hvacrawler.persistence.dao.ChestDAO;
import nl.hva.hvacrawler.persistence.dao.JdbcRoomDao;
import nl.hva.hvacrawler.persistence.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RoomRepositoryTest {
    @InjectMocks
    private RoomRepository  roomRepository;
    @Mock
    private JdbcRoomDao     roomDao;
    @Mock
    private ChestDAO        chestDAO;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOrUpdateOne() {
        Chest chest = new Chest();
        Room room = new Room();

        // stubbing is a way to simulate the behavior of code that a method depends on
        // so that the method can be tested in isolation.(thenReturn(T)
        when(chestDAO.saveOrUpdateOne(any())).thenReturn(chest);
        when(roomDao.saveOrUpdateOne(any())).thenReturn(room);

        roomRepository.saveOrUpdateOne(room);

        // Verify if the methods were called once
        verify(chestDAO, times(1)).saveOrUpdateOne(any());
        verify(roomDao, times(1)).saveOrUpdateOne(any());
    }


    @Test
    public void testSaveOrUpdateOne_DataIntegrityViolation() {
        Room room = new Room();
        when(chestDAO.saveOrUpdateOne(any(Chest.class))).thenThrow(DataIntegrityViolationException.class);
        roomRepository.saveOrUpdateOne(room);
        verify(chestDAO, times(1)).saveOrUpdateOne(any(Chest.class));
        verify(roomDao, times(0)).saveOrUpdateOne(any(Room.class));
    }

    @Test
    public void testGetRoomById() {
        Room room = new Room();
        when(roomDao.findOneById(anyInt())).thenReturn(Optional.of(room));

        Optional<Room> returnedRoom = roomRepository.getRoomById(1);
        verify(roomDao, times(1)).findOneById(1);
        assert(returnedRoom.isPresent());
    }
}

