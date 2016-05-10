 -- create database Monopoly;

use Monopoly;
/*
create table Player(
	username VARCHAR(255) NOT NULL PRIMARY KEY,
	password VARCHAR(255) NOT NULL,
	position INT(2),
	token VARCHAR(255),
	money INT(5) 
);


INSERT INTO Player VALUES('altudor', 'default', 0, null, 0);
INSERT INTO Player VALUES('daniculescu', 'default', 0, null, 0);
INSERT INTO Player VALUES('flmatei', 'default', 0, null, 0);
INSERT INTO Player VALUES('aprecupetu', 'default', 0, null, 0);
INSERT INTO Player VALUES('achesnoiu', 'default', 0, null, 0);
INSERT INTO Player VALUES('achirinus', 'default', 0, null, 0);
INSERT INTO Player VALUES('odobrescu', 'default', 0, null, 0);
INSERT INTO Player VALUES('cmaftei', 'default', 0, null, 0);
INSERT INTO Player VALUES('micristea', 'default', 0, null, 0);
INSERT INTO Player VALUES('adelush', 'default', 0, null, 0);
INSERT INTO Player VALUES('eldodu', 'default', 0, null, 0);
INSERT INTO Player VALUES('mcandea', 'default', 0, null, 0);


ALTER TABLE player 
ADD COLUMN isLogged BOOL DEFAULT 0;

DELETE FROM player
WHERE username = 'STINGSTER';


create table Commodity(
	id INT(2) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(255),
	price INT(4),
	rent INT(2),
	mortagage INT(2),
	owner VARCHAR(255),
	FOREIGN KEY (owner) REFERENCES Player(username) 
	ON DELETE SET NULL
	ON UPDATE CASCADE
);
/*




