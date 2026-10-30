DROP DATABASE IF EXISTS wishlist_db;
CREATE DATABASE wishlist_db;

USE wishlist_db;

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL
);

CREATE TABLE wish_list (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           title VARCHAR(100) NOT NULL,
                           share_code VARCHAR(36) NOT NULL UNIQUE,
                           user_id INT NOT NULL,
                           FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE wish (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      description VARCHAR(255),
                      price DOUBLE,
                      link_url VARCHAR(500),
                      picture_url VARCHAR(500),
                      wish_list_id INT NOT NULL,
                      FOREIGN KEY (wish_list_id) REFERENCES wish_list(id) ON DELETE CASCADE
);