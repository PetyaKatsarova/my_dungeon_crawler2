package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;
import java.util.List;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */

public class Game {
    private final Logger        logger = LoggerFactory.getLogger(Game.class);
    private int                 id;
    private Room[][]            gameBoard;
    private int                 rows;
    private int                 columns;
    @JsonIgnore
    private int                 finalScore;
    @JsonIgnore
    private Date                finishedAt;
    private Crawler             gameOwner;
    @JsonIgnore
    private GameStatus          gameStatus;
    public enum                 GameStatus { ONGOING, PAUSED, FINISHED }
    private static final int    ROWS_BOARD_DEFAULT = 3;
    private static final int    COLUMNS_BOARD_DEFAULT = 3;

    public Game() {
        this(null);
    }

    public Game(Crawler gameOwner){
        this(gameOwner, ROWS_BOARD_DEFAULT, COLUMNS_BOARD_DEFAULT);
    }

    public Game(Crawler gameOwner, int rows, int columns) {
        this.id = 0; // in db is auto increment: will be set later, after saving in db
        this.gameBoard = new Room[rows][columns];
        this.finalScore = 0;
        this.finishedAt = null;
        this.rows = rows;
        this.columns = columns;
        initGameboard(rows, columns);
        putGrailInRoom(rows, columns);
        Room currentRoom = this.gameBoard[rows/2][columns/2];
        currentRoom.setCurrentRoom(true);
        this.gameOwner = gameOwner;
        this.gameStatus = GameStatus.ONGOING;
        logger.info("New game");
    }

    private void initGameboard(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                List<Door> doorsForRoom = getDoorsForRoom(i, j, rows, columns);
                Room room = new Room(this, doorsForRoom);
                room.setRow(i);
                room.setColumn(j);
                gameBoard[i][j] = room;
            }
        }
    }

    // it is not private so could be tested
    public List<Door> getDoorsForRoom(int i, int j, int rows, int columns) {
        List<Door> doorsForRoom = new ArrayList<>();
        if (i > 0)
            doorsForRoom.add(Door.NORTH);
        if (i < rows - 1)
            doorsForRoom.add(Door.SOUTH);
        if (j > 0)
            doorsForRoom.add(Door.WEST);
        if (j < columns - 1)
            doorsForRoom.add(Door.EAST);
        return doorsForRoom;
    }

    //TODO check of die niet in de middelste kamer is waar user start.
    public void putGrailInRoom(int rows, int columns) {
        int grailX = (int) (Math.random() * rows);
        int grailY = (int) (Math.random() * columns);
        this.gameBoard[grailX][grailY].setContainsGrail(true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Room[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Room[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Crawler getGameOwner() {
        return gameOwner;
    }

    public void setGameOwner(Crawler gameOwner) {
        this.gameOwner = gameOwner;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override public String toString() {
        return "Game{" +
                "id=" + id +
                ", gameBoard=" + Arrays.toString(gameBoard) +
                ", rows=" + rows +
                ", columns=" + columns +
                ", finalScore=" + finalScore +
                ", finishedAt=" + finishedAt +
                ", gameOwner=" + gameOwner +
                ", gameStatus=" + gameStatus +
                '}';
    }
}