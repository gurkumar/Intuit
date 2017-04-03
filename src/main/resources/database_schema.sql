CREATE TABLE IF NOT EXISTS `tic_tac_toe_game` (
  `game_id` INT NOT NULL AUTO_INCREMENT,
  `row_data` ARRAY  NULL,
  `col_data` ARRAY  NULL,
  `diagnol_col_1` INT NULL DEFAULT 0,
  `diagnol_col_2` INT NULL DEFAULT 0,
  `state` VARCHAR(255) NULL,
  `moves_left` INT NULL,
  `game_size`  INT NULL,
  `available_cell_data` ARRAY NULL,
  PRIMARY KEY (`game_id`) )
;

CREATE TABLE IF NOT EXISTS `tic_tac_toe_player` (
  `game_id` INT NOT NULL,
  `player_id` CHAR NOT NULL,
  `turn` TINYINT(4)  NULL DEFAULT 0,
  `state` VARCHAR(255) NULL,
  PRIMARY KEY (`game_id`, `player_id`), 
   CONSTRAINT `tic_tac_toe_player_fk`
    FOREIGN KEY (`game_id`)
    REFERENCES `tic_tac_toe_game` (`game_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;