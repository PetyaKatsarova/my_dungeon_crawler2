package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.MonsterDTO;
import nl.hva.hvacrawler.persistence.dao.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class RoomRepository {
    private final JdbcRoomDao   roomDao;
    private final ChestDAO      chestDAO;
    private final JdbcMonsterDao monsterDao;
    private final JdbcWeaponDao weaponDao;
    private final JdbcGoldDao goldDao;
    private final JdbcTrapDao trapDao;
    private final JdbcPotionDao potionDao;
    private final Logger        logger = LoggerFactory.getLogger(RoomRepository.class);

    public RoomRepository(JdbcRoomDao roomDao, ChestDAO chestDAO, JdbcMonsterDao monsterDao,
                          JdbcWeaponDao weaponDao, JdbcGoldDao goldDao, JdbcTrapDao trapDao,
                          JdbcPotionDao potionDao) {
        this.roomDao = roomDao;
        this.chestDAO = chestDAO;
        this.monsterDao = monsterDao;
        this.weaponDao = weaponDao;
        this.goldDao = goldDao;
        this.trapDao = trapDao;
        this.potionDao = potionDao;
    }

    public Room saveOrUpdateOne(Room room) {
        try {
            Chest chest = chestDAO.saveOrUpdateOne(room.getChest());
                if (chest != null)
                    room.setChest(chest);
            return roomDao.saveOrUpdateOne(room);
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation in roomRepo.saveOrUpdateOne: ", e);
        }
        return null;
    }

    public Optional<Room> getRoomById(int id) {
        return roomDao.findOneById(id);
    }

    public int getHighestColumnByGameId(int idGame) {
        return roomDao.getHighestColumnByGameId(idGame);
    }

    public int getHighestRowByGameId(int idGame) {
        return roomDao.getHighestRowByGameId(idGame);
    }

    public Optional<Room> findOneById(int id) {
        return roomDao.findOneById(id);
    }

    public List<Room> findAllRoomsFromIdGame(int idGame) {
        List<Room> roomList = roomDao.findAllRoomsFromIdGame(idGame);
        for(Room room:roomList){
            int idRoom = room.getId();
            MonsterDTO monsterData = roomDao.getMonsterDataByRoomId(idRoom);
            if (monsterData.getIdCharacter() != 0){
                Optional<Monster> monsterOptional =
                        monsterDao.getMonsterById(monsterData.getIdCharacter());
                Monster monster = monsterOptional.get();
                monster.setHealthPoints(monsterData.getHealthPoints());
                monster.setGold(monsterData.getGold());
                room.setMonster(monster);
            } else{
                room.setMonster(null);
            }
            int idChest = roomDao.getIdChestByIdRoom(idRoom);
                if (idChest != -1){
                    room.setChest(new Chest());
                    room.getChest().setId(idChest);
                int idItem = chestDAO.getIdItemByIdChest(idChest);
                if (idItem <= 25) {
                    Optional<Weapon> weapon = weaponDao.getWeaponById(idItem);
                    room.getChest().setItem(weapon.get());
                } else if (idItem <= 35) {
                    Optional<Gold> gold = goldDao.getGoldById(idItem);
                    room.getChest().setItem(gold.get());
                } else if (idItem <= 40) {
                    Optional<Potion> potion = potionDao.getPotionById(idItem);
                    room.getChest().setItem(potion.get());
                } else if (idItem <= 43) {
                    Optional<Trap> trap = trapDao.getTrapById(idItem);
                    room.getChest().setItem(trap.get());
                } else {
                    room.getChest().setItem(new Nothing());
                }
            }else {
                    room.setChest(null);
                }
            int idItem = roomDao.getIdDroppedItemByIdRoom(idRoom);
            if (idItem != -1) {
                if (idItem <= 25) {
                    Optional<Weapon> weapon = weaponDao.getWeaponById(idItem);
                    room.setDroppedItem(weapon.get());
                } else if (idItem <= 35) {
                    Optional<Gold> gold = goldDao.getGoldById(idItem);
                    room.setDroppedItem(gold.get());
                } else if (idItem <= 40) {
                    Optional<Potion> potion = potionDao.getPotionById(idItem);
                    room.setDroppedItem(potion.get());
                } else {
                    Optional<Trap> trap = trapDao.getTrapById(idItem);
                    room.setDroppedItem(trap.get());
                }
            }
        }

        return roomList;
    }
}
