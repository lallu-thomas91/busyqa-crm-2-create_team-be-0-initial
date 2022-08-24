-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema usersdb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema usersdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `usersdb` DEFAULT CHARACTER SET utf8 ;
USE `usersdb` ;

-- -----------------------------------------------------
-- Table `usersdb`.`USER`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`USER` (
  `ID_USER` INT NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` VARCHAR(45) NOT NULL,
  `LAST_NAME` VARCHAR(45) NOT NULL,
  `USERNAME` VARCHAR(45) NOT NULL,
  `PASSWORD` VARCHAR(45) NOT NULL,
  `BIRTH` DATETIME NOT NULL,
  `STATUS` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`ID_USER`),
  UNIQUE INDEX `USERNAME_UNIQUE` (`USERNAME` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usersdb`.`ROLE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`ROLE` (
  `ID_ROLE` INT NOT NULL AUTO_INCREMENT,
  `NAME_ROLE` VARCHAR(20) NOT NULL,
  `DESC_ROLE` VARCHAR(45) NOT NULL,
  `STATUS` VARCHAR(1) NOT NULL,
  PRIMARY KEY (`ID_ROLE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `usersdb`.`USER_ROLE`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `usersdb`.`USER_ROLE` (
  `ID_USER_ROLE` INT NOT NULL AUTO_INCREMENT,
  `ID_USER` INT NOT NULL,
  `ID_ROLE` INT NOT NULL,
  PRIMARY KEY (`ID_USER_ROLE`),
  INDEX `UR_ID_ROLE_IDX` (`ID_ROLE` ASC),
  INDEX `UR_ID_USER_IDX` (`ID_USER` ASC),
  CONSTRAINT `FK_UR_ID_USER`
    FOREIGN KEY (`ID_USER`)
    REFERENCES `usersdb`.`USER` (`ID_USER`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_UR_ID_ROLE`
    FOREIGN KEY (`ID_ROLE`)
    REFERENCES `usersdb`.`ROLE` (`ID_ROLE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
