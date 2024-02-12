package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.GameCrawlerDTO;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.communication.dto.GameRoomDTO;
import nl.hva.hvacrawler.persistence.repository.*;
import org.springframework.stereotype.Service;

@Service
public class SaveService {

    private final GameRepository                gameRepository;
    private final RoomRepository                roomRepository;
    private final CrawlerRepository             crawlerRepository;
    private final UserRepository                userRepository;
    private final CrawlerHasPotionRepository    crawlerHasPotionRepository;

    public SaveService(GameRepository gameRepository, RoomRepository roomRepository, CrawlerRepository crawlerRepository,
                       UserRepository userRepository, CrawlerHasPotionRepository crawlerHasPotionRepository) {
        this.gameRepository = gameRepository;
        this.roomRepository = roomRepository;
        this.crawlerRepository = crawlerRepository;
        this.userRepository = userRepository;
        this.crawlerHasPotionRepository = crawlerHasPotionRepository;
    }

    public GameDTO handleSave(GameDTO gameDTO) {
        Game game = GameDTOToGameConverter(gameDTO);
        Game gameWithId = gameRepository.saveOrUpdateGame(game);
        gameDTO.setId(gameWithId.getId());
//       update room's ids in the the gamedto
        Room[][] gameboard = gameWithId.getGameBoard();
        GameRoomDTO[][] gameboardDTO = gameDTO.getGameBoard();
        for (int i = 0; i < gameWithId.getRows(); i++) {
            for (int j = 0; j < gameWithId.getColumns(); j++) {
                GameRoomDTO room = gameboardDTO[i][j];
                room.setId(gameboard[i][j].getId());
            }
        }
        gameDTO.setGameBoard(gameboardDTO);
        // save in crawlerHasPotion
        return gameDTO;
    }

    private Game GameDTOToGameConverter(GameDTO gameDTO){
        Game game = new Game();

        game.setId(gameDTO.getId());
        game.setRows(gameDTO.getRows());
        game.setColumns(gameDTO.getColumns());
        Crawler crawler = creatingCrawler(gameDTO.getGameOwner());
        game.setGameOwner(crawler);
        game.setGameStatus(Game.GameStatus.PAUSED);

        Room[][] gameboard = new Room[gameDTO.getRows()][gameDTO.getColumns()];
        GameRoomDTO[][] gameBoardDTO = gameDTO.getGameBoard();
        for (int i = 0; i < gameDTO.getRows(); i++) {
            for (int j = 0; j < gameDTO.getColumns(); j++) {
                GameRoomDTO gameRoomDTO = gameBoardDTO[i][j];
                Room room = makingRoomFromDTO(gameRoomDTO);
                room.setGame(game);
                roomRepository.saveOrUpdateOne(room);
                gameboard[i][j] = room;
            }
        }
        game.setGameBoard(gameboard);

        return game;
    }

    private Room makingRoomFromDTO(GameRoomDTO gameRoomDTO){
        Room room = new Room();
        room.setId(gameRoomDTO.getId());
        room.setRow(gameRoomDTO.getRow());
        room.setColumn(gameRoomDTO.getColumn());
        room.setChest(gameRoomDTO.getChest());
        room.setDroppedItem(gameRoomDTO.getDroppedItem());
        room.setDroppedItem(gameRoomDTO.getDroppedItem());
        room.setMonster(gameRoomDTO.getMonster());
        room.setVisited(gameRoomDTO.isVisited());
        room.setCurrentRoom(gameRoomDTO.isCurrentRoom());
        room.setContainsGrail(gameRoomDTO.isContainsGrail());
        room.setDoors(gameRoomDTO.getDoors());
        return room;
    }

//    private Item makingItem(ItemDTO itemDTO){
//        Item item;
//        int id = itemDTO.getId();
//        if(id >= 1 && id <= 25){
//            item = new Weapon();
//            item.setAttackModifier(itemDTO.getModifierValue());
//        } else if (id >= 26 && id <= 35) {
//            item = new Gold();
//            item.setValue(itemDTO.getModifierValue());
//        } else if (id >= 36 && id < 40){
//            item = new Potion();
//            item.setHealingModifier(itemDTO.getModifierValue());
//        } else {
//            item = new Trap();
//            item.setDamage(itemDTO.getModifierValue());
//        }
//        item.setName(itemDTO.getName());
//        item.setId(itemDTO.getId());
//        return item;
//    }

    private Crawler creatingCrawler(GameCrawlerDTO gameCrawlerDTO){
        Crawler crawler = makingCrawlerFromDTO(gameCrawlerDTO);
        int userId = crawlerRepository.getUserIdByCrawlerId(crawler.getIdCharacter());
        User user = userRepository.findUserById(userId);
        crawler.setUser(user);
        return crawler;
    }

    private Crawler makingCrawlerFromDTO(GameCrawlerDTO gameCrawlerDTO){
        Crawler crawler = new Crawler();
        crawler.setIdCharacter(gameCrawlerDTO.getIdCharacter());
        crawler.setName(gameCrawlerDTO.getName());
        crawler.setHealthPoints(gameCrawlerDTO.getHealthPoints());
        crawler.setGold(gameCrawlerDTO.getGold());
        crawler.setKills(gameCrawlerDTO.getKills());
        crawler.setRoomsVisited(gameCrawlerDTO.getRoomsVisited());
        crawler.setHealthPotions(gameCrawlerDTO.getHealthPotions());
        crawler.setWeapon(gameCrawlerDTO.getWeapon());
        return crawler;
    }
}
