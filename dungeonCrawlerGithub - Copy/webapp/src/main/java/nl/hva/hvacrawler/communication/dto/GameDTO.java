package nl.hva.hvacrawler.communication.dto;

import nl.hva.hvacrawler.business.domain.Game;

import java.util.Arrays;

public class GameDTO {
    public enum GameStatus { ONGOING, PAUSED, FINISHED }
    private int id;
    private GameRoomDTO[][] gameBoard;
    private int rows;
    private int columns;
    private GameCrawlerDTO gameOwner;
    private GameStatus gameStatus;
    public GameDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameRoomDTO[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameRoomDTO[][] gameBoard) {
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

    public GameCrawlerDTO getGameOwner() {
        return gameOwner;
    }

    public void setGameOwner(GameCrawlerDTO gameOwner) {
        this.gameOwner = gameOwner;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    @Override public String toString() {
        return "GameDTO{" +
                "id=" + id +
                ", gameBoard=" + Arrays.toString(gameBoard) +
                ", rows=" + rows +
                ", columns=" + columns +
                ", gameOwner=" + gameOwner +
                '}';
    }
}