CREATE TABLE lostpassword (
   id INTEGER IDENTITY PRIMARY KEY,
   username VARCHAR(100),
   expiration TIMESTAMP,
   token VARCHAR(100)
);

CREATE TABLE category (
   id INTEGER PRIMARY KEY,
   label VARCHAR(100)
);

INSERT INTO category VALUES (1, 'Bouchon de vin');
INSERT INTO category VALUES (2, 'Bouchon de champagne');
INSERT INTO category VALUES (3, 'Bouchon de bière');
INSERT INTO category VALUES (4, 'Bouchon de flacon');


CREATE TABLE material (
   id INTEGER PRIMARY KEY,
   label VARCHAR(100)
);

INSERT INTO material VALUES (1, 'Liège naturel');
INSERT INTO material VALUES (2, 'Liège colmaté');
INSERT INTO material VALUES (3, 'Liège aggloméré');
INSERT INTO material VALUES (4, 'Synthétique');
INSERT INTO material VALUES (5, 'Caoutchouc');
INSERT INTO material VALUES (6, 'Verre');

CREATE TABLE product (
   id INTEGER IDENTITY PRIMARY KEY,
   label VARCHAR(100),
   description VARCHAR(500),
   category INTEGER,
   material INTEGER,
   diameter INTEGER,
   height INTEGER,
   price FLOAT,
   img VARCHAR(100),
   FOREIGN KEY (category) REFERENCES category (id),
   FOREIGN KEY (material) REFERENCES material (id)
);

/* vin 75 cl */
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin haut de gamme en liège (75cl)', 'Bouchon de qualité Sup, idéal pour toutes les bouteilles de vin 75cl. Découpé directement dans les plaques de liège prélevées sur l''arbre à liège. Le liège permet de faire respirer le vin et garantit une bonne évolution dans le temps. Recommandé pour les vins de garde', 1, 1, 24, 44, 20, 'bouchonliege.jpg');
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin standard en liège (75cl)', 'Bouchon en liège colmaté convenable pour toutes les bouteilles de vin 75cl. Fabriqué en liège naturel dont les trous ont été colmaté avec un mélange de colle et de poudre de liège, augmentant la qualité d''obturation. Recommandé pour les vins de garde moyenne (5 ans maximum)', 1, 2, 24, 44, 10, 'bouchoncolmat.jpg');
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin low-cost en liège (75cl)', 'Bouchon en liège aggloméré issu de chute de bouchons en liège naturel, limitant la respiration. Recommandé pour les vins à boire dans l''année', 1, 3, 24, 44, 8, 'bouchonagglo.jpg');

INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin synthétique (75cl)', 'Bouchon synthétique convenable pour toutes les bouteilles de vin 75cl. Permet un échange air-vin et donc le vieillissement du vin. Recommandé pour les vins de garde moyenne', 1, 3, 24, 44, 15, 'bouchonresine.jpg');
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin low-cost synthétique (75cl)', 'Bouchon synthétique injecté convenable pour toutes les bouteilles de vin 75cl. Recommandé pour les vins à boire jeune', 1, 3, 24, 44, 8, 'bouchonresine.jpg');

INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin en verre (75cl)', 'Nouveau type de bouchon en verre totalement étanche, donc sans respiration, pour les bouteilles de vin 75cl. Ne convient que pour les vins à boire dans l''année !', 1, 5, 24, 44, 5, 'bouchonverre.jpg');

/* vin 1,5l (magnum) */
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin haut de gamme en liège (Magnum)', 'Bouchon de qualité Sup, idéal pour toutes les bouteilles de vin magnum de 1,5l. Découpé directement dans les plaques de liège prélevées sur l''arbre à liège. Le liège permet de faire respirer le vin et garantit une bonne évolution dans le temps. Recommandé pour les vins de garde', 1, 1, 26, 49, 25, 'bouchonliege.jpg');
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin standard en liège (Magnum)', 'Bouchon en liège colmaté convenable pour toutes les bouteilles de vin magnum de 1,5l. Fabriqué en liège naturel dont les trous ont été colmaté avec un mélange de colle et de poudre de liège, augmentant la qualité d''obturation. Recommandé pour les vins de garde moyenne (5 ans maximum)', 1, 2, 26, 49, 15, 'bouchonagglo.jpg');

INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin synthétique (Magnum)', 'Bouchon synthétique convenable pour toutes les bouteilles de vin 75cl. Permet un échange air-vin et donc le vieillissement du vin. Recommandé pour les vins de garde moyenne', 1, 3, 26, 49, 20, 'bouchonresine.jpg');

/* vin 3l (jéroboam) */
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin haut de gamme en liège (Jéroboam)', 'Bouchon de qualité Sup, idéal pour toutes les bouteilles de vin jéroboam de 3l. Découpé directement dans les plaques de liège prélevées sur l''arbre à liège. Le liège permet de faire respirer le vin et garantit une bonne évolution dans le temps. Recommandé pour les vins de garde', 1, 1, 30, 53, 30, 'bouchonliege.jpg');
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin standard en liège (Jéroboam)', 'Bouchon en liège colmaté convenable pour toutes les bouteilles de vin jéroboam de 3l. Fabriqué en liège naturel dont les trous ont été colmaté avec un mélange de colle et de poudre de liège, augmentant la qualité d''obturation. Recommandé pour les vins de garde moyenne (5 ans maximum)', 1, 2, 30, 53, 20, 'bouchonagglo.jpg');

INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin synthétique (Jéroboam)', 'Bouchon synthétique convenable pour toutes les bouteilles de vin jéroboam de 3l. Permet un échange air-vin et donc le vieillissement du vin. Recommandé pour les vins de garde moyenne', 1, 3, 30, 53, 25, 'bouchonresine.jpg');

/* vin 6l (mathusalem) */
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de vin haut de gamme en liège (Mathusalem)', 'Bouchon de qualité Sup, idéal pour toutes les bouteilles de vin Mathusalem de 6l. Découpé directement dans les plaques de liège prélevées sur l''arbre à liège. Le liège permet de faire respirer le vin et garantit une bonne évolution dans le temps. Recommandé pour les vins de garde', 1, 1, 33, 53, 40, 'bouchonliege.jpg');

/* champagne 75 cl */
INSERT INTO product (label, description, category, material, diameter, height, price, img) VALUES ('Bouchon de champagne haut de gamme en liège (75cl)', 'Bouchon de qualité Sup, idéal pour toutes les bouteilles de champagne 75cl. Découpé directement dans les plaques de liège prélevées sur l''arbre à liège.', 2, 1, 31, 48, 100, 'bouchonchampagne.jpg');

CREATE TABLE usercomment (
   id INTEGER IDENTITY PRIMARY KEY,
   product INTEGER,
   username VARCHAR(100),
   mark INTEGER,
   datetime DATE,
   content VARCHAR(500),
   FOREIGN KEY (product) REFERENCES product (id)
);

INSERT INTO usercomment (product, username, mark, datetime, content) VALUES (0, 'bob', 5, DATE '2017-04-01', 'Bouchon très performant !');
INSERT INTO usercomment (product, username, mark, datetime, content) VALUES (2, 'bob', 5, DATE '2016-04-01', 'Un bouchon comme on les aime !');
INSERT INTO usercomment (product, username, mark, datetime, content) VALUES (9, 'bob', 1, DATE '2017-04-01', 'J''ai acheté ce bouchon et il ne rentre pas dans mes bouteilles :(');


CREATE TABLE users (
  id  INTEGER IDENTITY PRIMARY KEY,
  username VARCHAR(30),
  password  VARCHAR(50)
);

INSERT INTO users (username, password) VALUES ('user', 'password');
INSERT INTO users (username, password) VALUES ('bob', 'éponge');



