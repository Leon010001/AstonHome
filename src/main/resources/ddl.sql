-- Таблица Разработчиков (Developers)
CREATE TABLE Developers (
                            developer_id SERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            foundation_year INT
);

-- Таблица Игр (Games)
CREATE TABLE Games (
                       game_id SERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       developer_id INT,
                       release_year INT,
                       genre VARCHAR(100),
                       FOREIGN KEY (developer_id) REFERENCES Developers(developer_id) ON DELETE SET NULL
);

-- Таблица Игроков (Players)
CREATE TABLE Players (
                         player_id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE,
                         registration_date DATE DEFAULT CURRENT_DATE
);

-- Таблица Покупок (Purchases)
CREATE TABLE Purchases (
                           purchase_id SERIAL PRIMARY KEY,
                           game_id INT NOT NULL,
                           player_id INT NOT NULL,
                           purchase_date DATE DEFAULT CURRENT_DATE,
                           FOREIGN KEY (game_id) REFERENCES Games(game_id) ON DELETE CASCADE,
                           FOREIGN KEY (player_id) REFERENCES Players(player_id) ON DELETE CASCADE
);