

CREATE TABLE users (
  username VARCHAR(50) NOT NULL PRIMARY KEY,
  password VARCHAR(500) NOT NULL,
  enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
  username VARCHAR(50) NOT NULL,
  authority VARCHAR(50) NOT NULL,
  CONSTRAINT fk_authorities_users 
    FOREIGN KEY (username) REFERENCES users (username)
    ON DELETE CASCADE,
  PRIMARY KEY (username, authority)
);

CREATE UNIQUE INDEX ix_auth_username 
ON authorities (username, authority);

-- Users table inserts
INSERT IGNORE INTO `users` (`username`, `password`, `enabled`) 
VALUES 
  ('user', '{noop}Blue!Tiger@47Sun', 1),
  ('admin', '{bcrypt}$2a$12$kDn2lQ/lobS3.Ucwlv6CBeIfDAskRXWSBBv6JIO1pQYTekeI6dBJe', 1);

-- Authorities table inserts
INSERT IGNORE INTO `authorities` (`username`, `authority`) 
VALUES 
  ('user', 'read'),
  ('admin', 'admin');



CREATE TABLE `customer` (
    `id` int NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
    `pwd` VARCHAR(500) NOT NULL,
    `role` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
);

INSERT INTO `customer` (`email`, `pwd`, `role`) 
VALUES 
  ('happy@gmail.com', '{noop}Blue!Tiger@47Sun', 'read'),
  ('admin@gmail.com', '{bcrypt}$2a$12$kDn2lQ/lobS3.Ucwlv6CBeIfDAskRXWSBBv6JIO1pQYTekeI6dBJe', 'admin');