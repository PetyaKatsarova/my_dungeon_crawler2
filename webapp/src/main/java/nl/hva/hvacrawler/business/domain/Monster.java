package nl.hva.hvacrawler.business.domain;


public class Monster extends Character{


    public Monster() {
        this("Monster", 0, 0, 0);
    }

    public Monster(String name, int healthPoints, int goldPieces, int idCharacter) {
        super(name, healthPoints, goldPieces);
        setIdCharacter(idCharacter);
    }



}
