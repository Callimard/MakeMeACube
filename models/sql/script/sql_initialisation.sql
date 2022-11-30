-- MySQL Script generated by MySQL Workbench
-- Wed Nov 30 19:51:20 2022
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema makemeacube
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `makemeacube` ;

-- -----------------------------------------------------
-- Schema makemeacube
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `makemeacube` DEFAULT CHARACTER SET utf8 ;
USE `makemeacube` ;

-- -----------------------------------------------------
-- Table `makemeacube`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `makemeacube`.`User` ;

CREATE TABLE IF NOT EXISTS `makemeacube`.`User` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `mail` VARCHAR(255) NOT NULL,
  `pseudo` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `firstName` VARCHAR(255) NULL,
  `lastName` VARCHAR(255) NULL,
  `phone` VARCHAR(255) NULL,
  `isMaker` TINYINT(1) NOT NULL DEFAULT 0,
  `makerDescription` TEXT NULL,
  `provider` VARCHAR(255) NOT NULL,
  `creationDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `mail_UNIQUE` (`mail` ASC) VISIBLE,
  UNIQUE INDEX `pseudo_UNIQUE` (`pseudo` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `makemeacube`.`MakerTool`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `makemeacube`.`MakerTool` ;

CREATE TABLE IF NOT EXISTS `makemeacube`.`MakerTool` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `owner` INT NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `description` VARCHAR(2000) NOT NULL,
  `reference` VARCHAR(3000) NULL,
  `makerToolType` VARCHAR(5) NOT NULL,
  `quantity` TINYINT(3) UNSIGNED NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  INDEX `fk_MakerTool_User1_idx` (`owner` ASC) VISIBLE,
  UNIQUE INDEX `owner_name_UNIQUE` (`owner` ASC, `name` ASC) VISIBLE,
  CONSTRAINT `fk_MakerTool_User1`
    FOREIGN KEY (`owner`)
    REFERENCES `makemeacube`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `makemeacube`.`Printer3D`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `makemeacube`.`Printer3D` ;

CREATE TABLE IF NOT EXISTS `makemeacube`.`Printer3D` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `x` SMALLINT(5) NOT NULL,
  `y` SMALLINT(5) NOT NULL,
  `z` SMALLINT(5) NOT NULL,
  `xAccuracy` SMALLINT(5) NOT NULL,
  `yAccuracy` SMALLINT(5) NOT NULL,
  `zAccuracy` SMALLINT(5) NOT NULL,
  `resolution` VARCHAR(45) NULL,
  `type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_Printer3D_MakerTool1`
    FOREIGN KEY (`id`)
    REFERENCES `makemeacube`.`MakerTool` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `makemeacube`.`UserAddress`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `makemeacube`.`UserAddress` ;

CREATE TABLE IF NOT EXISTS `makemeacube`.`UserAddress` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(200) NOT NULL,
  `city` VARCHAR(200) NOT NULL,
  `country` VARCHAR(100) NOT NULL,
  `postalCode` VARCHAR(20) NOT NULL,
  `user` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_UserAddress_User1_idx` (`user` ASC) VISIBLE,
  CONSTRAINT `fk_UserAddress_User1`
    FOREIGN KEY (`user`)
    REFERENCES `makemeacube`.`User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `makemeacube`.`Material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `makemeacube`.`Material` ;

CREATE TABLE IF NOT EXISTS `makemeacube`.`Material` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NOT NULL,
  `colors` VARCHAR(1000) NULL,
  `description` VARCHAR(1000) NULL,
  `tool` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Material_MakerTool1_idx` (`tool` ASC) VISIBLE,
  CONSTRAINT `fk_Material_MakerTool1`
    FOREIGN KEY (`tool`)
    REFERENCES `makemeacube`.`MakerTool` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
