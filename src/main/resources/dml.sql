-- Вставка тестовых данных в таблицу Developers
INSERT INTO Developers (name, foundation_year) VALUES ('Valve', 2000);
INSERT INTO Developers (name, foundation_year) VALUES ('Electronic arts', 1995);
INSERT INTO Developers (name, foundation_year) VALUES ('Blizzard', 2010);

-- Вставка тестовых данных в таблицу Games
INSERT INTO Games (title, developer_id, release_year, genre) VALUES ('Counter-strike', 1, 2015, 'Shooter');
INSERT INTO Games (title, developer_id, release_year, genre) VALUES ('FIFA 24', 2, 2023, 'Sports');
INSERT INTO Games (title, developer_id, release_year, genre) VALUES ('World of warcraft', 3, 2005, 'Strategy');

-- Вставка тестовых данных в таблицу Players
INSERT INTO Players (name, email) VALUES ('John Brown', 'john@example.com');
INSERT INTO Players (name, email) VALUES ('Donal Trump', 'trump@example.com');
INSERT INTO Players (name, email) VALUES ('Alice Jackson', 'alice@example.com');

-- Вставка тестовых данных в таблицу Purchases
INSERT INTO Purchases (game_id, player_id, purchase_date) VALUES (1, 1, '2023-10-01');
INSERT INTO Purchases (game_id, player_id, purchase_date) VALUES (2, 2, '2023-10-05');
INSERT INTO Purchases (game_id, player_id, purchase_date) VALUES (3, 3, '2023-10-10');-- Alice покупает ту же игру дважды