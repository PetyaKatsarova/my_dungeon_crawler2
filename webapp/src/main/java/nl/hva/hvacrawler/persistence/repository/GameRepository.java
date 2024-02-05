package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.HasPotionDTO;
import nl.hva.hvacrawler.exception.CrawlerNotFoundException;
import nl.hva.hvacrawler.persistence.dao.JdbcCrawlerDao;
import nl.hva.hvacrawler.persistence.dao.JdbcCrawlerHasPotionDao;
import nl.hva.hvacrawler.persistence.dao.JdbcGameDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 14/08/2023 13:53
 */

@Repository
public class GameRepository {
    private final Logger logger = LoggerFactory.getLogger(GameRepository.class);
    private final JdbcGameDao               jdbcGameDao;
    private final RoomRepository            roomRepository;
    private final JdbcCrawlerDao            jdbcCrawlerDao;
    private final CrawlerRepository         crawlerRepository;
    private final JdbcCrawlerHasPotionDao   jdbcCrawlerHasPotionDao;

    public GameRepository(JdbcGameDao jdbcGameDao, RoomRepository roomRepository,
                          JdbcCrawlerDao jdbcCrawlerDao, CrawlerRepository crawlerRepository,
                          JdbcCrawlerHasPotionDao jdbcCrawlerHasPotionDao) {
        this.jdbcGameDao = jdbcGameDao;
        this.roomRepository = roomRepository;
        this.jdbcCrawlerDao = jdbcCrawlerDao;
        this.crawlerRepository = crawlerRepository;
        this.jdbcCrawlerHasPotionDao = jdbcCrawlerHasPotionDao;
        logger.info("New GameRepository");
    }

    public Game saveOrUpdateGame(Game game) {
        Game gameWithId = jdbcGameDao.saveOrUpdateOne(game);
        Room[][] gameboard = game.getGameBoard();
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                Room room = gameboard[i][j];
                room.setGame(game);
                Room roomWithId = roomRepository.saveOrUpdateOne(room);
                room.setId(roomWithId.getId());
            }
        }
        gameWithId.setGameBoard(gameboard);
        List<Potion> potions = game.getGameOwner().getHealthPotions();
        int idCrawler = game.getGameOwner().getIdCharacter();
        jdbcCrawlerHasPotionDao.deleteCrawlerHasPotionByCrawlerId(idCrawler);
        for (Potion p : potions) {
            HasPotionDTO hasPotionDTO = new HasPotionDTO(idCrawler, p.getId(), 1);
            jdbcCrawlerHasPotionDao.saveOrUpdateOne(hasPotionDTO);
        }
        jdbcCrawlerDao.saveOrUpdateOne(game.getGameOwner());
        return gameWithId;
    }

    public Game findGameById(int id) {
        Optional<Game> gameOptional = jdbcGameDao.findOneById(id);
        return gameOptional.orElse(null);
    }


    public Game resumeGame(User user) {
        Crawler crawler = crawlerRepository.findOneByUserId(user.getIdUser());
        if (crawler != null) {
            int idGame =
                    jdbcGameDao.getHighestPausedGameIdFromCrawlerId(crawler.getIdCharacter());
            int column = roomRepository.getHighestColumnByGameId(idGame) + 1;
            int row = roomRepository.getHighestRowByGameId(idGame) + 1;
            Game game = new Game(crawler, row, column);
            game.setId(idGame);
            List<Room> storedRooms = roomRepository.findAllRoomsFromIdGame(idGame);
            int roomListCounter = 0;
            Room[][] gameboard = game.getGameBoard();
            for (int i = 0; i < game.getRows(); i++) {
                for (int j = 0; j < game.getColumns(); j++) {
                    Room newRoom = gameboard[i][j];
                    Room storedRoom = storedRooms.get(roomListCounter);
                    roomListCounter++;
                    setRoom(newRoom, storedRoom);
                }
            }
            game.setGameBoard(gameboard);
            return game;
        }
        else{
            throw new CrawlerNotFoundException("your crawler is missing");
        }
    }

    private void setRoom(Room newRoom, Room storedRoom) {
        newRoom.setId(storedRoom.getId());
        newRoom.setContainsGrail(storedRoom.isContainsGrail());
        newRoom.setCurrentRoom(storedRoom.isCurrentRoom());
        newRoom.setVisited(storedRoom.isVisited());
        newRoom.setMonster(storedRoom.getMonster());
        newRoom.setChest(storedRoom.getChest());
        newRoom.setDroppedItem(storedRoom.getDroppedItem());
    }
}
