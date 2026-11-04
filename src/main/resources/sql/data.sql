INSERT INTO users (id, name, email, password) VALUES
                                                  (1, 'Anna', 'anna@test.dk', '1234'),
                                                  (2, 'Mads', 'mads@test.dk', '1234');


INSERT INTO wish_list (id, title, share_code, user_id) VALUES
                                                           (1, 'Annas fødselsdag', 'anna1', 1),
                                                           (2, 'Annas jul',        'anna2', 1),
                                                           (3, 'Mads ønsker',      'mads1', 2);


INSERT INTO wish (name, description, price, link_url, picture_url, wish_list_id) VALUES
                                                                                     ('Bluetooth-højttaler', 'Gerne sort',                 499.00, 'https://example.com/hoejttaler', 'https://example.com/hoejttaler.jpg', 1),
                                                                                     ('Rygsæk',              'Til gåture, ca. 25 liter',   699.95, 'https://example.com/rygsaek',    NULL,                                 1),
                                                                                     ('Bog om Java',         'Til studiet',                349.00, 'https://example.com/java-bog',   NULL,                                 1),
                                                                                     ('Uldsokker',           'Str. 39-42',                 129.00, NULL,                             NULL,                                 2),
                                                                                     ('Termokrus',           'Holder på varmen i 12 timer', 199.00, 'https://example.com/krus',      'https://example.com/krus.jpg',       2),
                                                                                     ('Løbesko',             'Str. 43',                    899.00, 'https://example.com/loebesko',   NULL,                                 3),
                                                                                     ('Gavekort til biograf', NULL,                        250.00, NULL,                             NULL,                                 3);