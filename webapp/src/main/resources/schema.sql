-- MySQL Script generated by MySQL Workbench
-- Thu Aug 24 13:11:07 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema HVACrawler_test
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `HVACrawler_test` ;

-- -----------------------------------------------------
-- Schema HVACrawler_test
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `HVACrawler_test` DEFAULT CHARACTER SET utf8 ;
USE `HVACrawler_test` ;

-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Chest`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Chest` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Chest` (
  `idChest` INT NOT NULL AUTO_INCREMENT,
  `idItem` INT NULL,
  PRIMARY KEY (`idChest`),
  INDEX `fk_Chest_Item1_idx` (`idItem` ASC) VISIBLE,
  CONSTRAINT `fk_Chest_Item1`
    FOREIGN KEY (`idItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Crawler`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Crawler` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Crawler` (
  `idCrawler` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL UNIQUE,
  `healthPoints` INT NOT NULL,
  `kills` INT NOT NULL,
  `roomsVisited` INT NOT NULL,
  `gold` INT NULL,
  `weapon` INT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idCrawler`),
  INDEX `fk_Player_User1_idx` (`idUser` ASC) VISIBLE,
  INDEX `fk_Crawler_Weapon1_idx` (`weapon` ASC) VISIBLE,
  CONSTRAINT `fk_Player_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `HVACrawler_test`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Crawler_Weapon1`
    FOREIGN KEY (`weapon`)
    REFERENCES `HVACrawler_test`.`Weapon` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Crawler_has_Potion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Crawler_has_Potion` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Crawler_has_Potion` (
  `idCrawler` INT NOT NULL,
  `idItem_Potion` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`idCrawler`, `idItem_Potion`),
  INDEX `fk_Crawler_has_Potion_Potion1_idx` (`idItem_Potion` ASC) VISIBLE,
  INDEX `fk_Crawler_has_Potion_Crawler1_idx` (`idCrawler` ASC) VISIBLE,
  CONSTRAINT `fk_Crawler_has_Potion_Crawler1`
    FOREIGN KEY (`idCrawler`)
    REFERENCES `HVACrawler_test`.`Crawler` (`idCrawler`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Crawler_has_Potion_Potion1`
    FOREIGN KEY (`idItem_Potion`)
    REFERENCES `HVACrawler_test`.`Potion` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Game` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Game` (
  `idGame` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(45) NOT NULL,
  `finalScore` INT NULL,
  `finishedAt` DATE NULL,
  `idCrawler` INT NOT NULL,
  PRIMARY KEY (`idGame`),
  INDEX `fk_Game_Player1_idx` (`idCrawler` ASC) VISIBLE,
  CONSTRAINT `fk_Game_Player1`
    FOREIGN KEY (`idCrawler`)
    REFERENCES `HVACrawler_test`.`Crawler` (`idCrawler`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Gold`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Gold` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Gold` (
  `idItem` INT NOT NULL,
  `value` INT NOT NULL,
  INDEX `fk_Gold_Item1_idx` (`idItem` ASC) VISIBLE,
  PRIMARY KEY (`idItem`),
  CONSTRAINT `fk_Gold_Item1`
    FOREIGN KEY (`idItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Item` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Item` (
  `idItem` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idItem`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Monster`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Monster` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Monster` (
  `idMonster` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `healthpoints` INT NOT NULL,
  PRIMARY KEY (`idMonster`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Potion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Potion` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Potion` (
  `idItem` INT NOT NULL,
  `healthPoints` INT NOT NULL,
  INDEX `fk_Gold_Item1_idx` (`idItem` ASC) VISIBLE,
  PRIMARY KEY (`idItem`),
  CONSTRAINT `fk_Gold_Item10`
    FOREIGN KEY (`idItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Room`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Room` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Room` (
  `idRoom` INT NOT NULL AUTO_INCREMENT,
  `row` INT NOT NULL,
  `column` INT NOT NULL,
  `holygrail` TINYINT NOT NULL,
  `currentRoom` TINYINT NOT NULL,
  `visitedRoom` TINYINT NOT NULL,
  `idGame` INT NOT NULL,
  `idMonster` INT NULL,
  `healthMonster` INT NULL,
  `goldMonster` INT NULL,
  `idChest` INT NULL,
  `droppedItem` INT NULL,
  PRIMARY KEY (`idRoom`),
  INDEX `fk_Room_Chest1_idx` (`idChest` ASC) VISIBLE,
  INDEX `fk_Room_Monster1_idx` (`idMonster` ASC) VISIBLE,
  INDEX `fk_Room_Game1_idx` (`idGame` ASC) VISIBLE,
  CONSTRAINT `fk_Room_Chest1`
    FOREIGN KEY (`idChest`)
    REFERENCES `HVACrawler_test`.`Chest` (`idChest`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Room_Monster1`
    FOREIGN KEY (`idMonster`)
    REFERENCES `HVACrawler_test`.`Monster` (`idMonster`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Room_Item1`
    FOREIGN KEY (`droppedItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Room_Game1`
    FOREIGN KEY (`idGame`)
    REFERENCES `HVACrawler_test`.`Game` (`idGame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Trap`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Trap` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Trap` (
  `idItem` INT NOT NULL,
  `damagePoints` INT NOT NULL,
  INDEX `fk_Gold_Item1_idx` (`idItem` ASC) VISIBLE,
  PRIMARY KEY (`idItem`),
  CONSTRAINT `fk_Gold_Item100`
    FOREIGN KEY (`idItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`User` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(70) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `salt` VARCHAR(45) NOT NULL,
  `isVerified` TINYINT NOT NULL,
  `resetToken` VARCHAR(6) NULL,
  `jwtToken` VARCHAR(255) NULL,
  `highscore` INT NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`VerificationToken`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`VerificationToken` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`VerificationToken` (
  `idVerificationToken` INT NOT NULL AUTO_INCREMENT,
  `token` VARCHAR(255) NOT NULL,
  `expirationTime` DATETIME(6) NOT NULL,
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idVerificationToken`, `idUser`),
  INDEX `fk_VerificationToken_User1_idx` (`idUser` ASC) VISIBLE,
  CONSTRAINT `fk_VerificationToken_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `HVACrawler_test`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `HVACrawler_test`.`Weapon`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `HVACrawler_test`.`Weapon` ;

CREATE TABLE IF NOT EXISTS `HVACrawler_test`.`Weapon` (
  `idItem` INT NOT NULL,
  `attackModifier` INT NOT NULL,
  INDEX `fk_Gold_Item1_idx` (`idItem` ASC) VISIBLE,
  PRIMARY KEY (`idItem`),
  CONSTRAINT `fk_Gold_Item101`
    FOREIGN KEY (`idItem`)
    REFERENCES `HVACrawler_test`.`Item` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

# -- Gebruiker definiëren en toegang verlenen
# CREATE USER IF NOT EXISTS 'GameMaster'@'localhost' IDENTIFIED BY 'PWGameMaster';
# GRANT ALL PRIVILEGES ON HVACrawler_test.* TO 'GameMaster'@'localhost';