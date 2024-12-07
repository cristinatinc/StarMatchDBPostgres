-- Insert data into User table
INSERT INTO "User" (name, email, password, birthDate, birthTime, birthPlace)
VALUES
    ('amna', 'amna@gmail.com', 'parola', '2000-03-12', '09:00:00', 'Cluj'),
    ('Florian', 'florinel@gmail.com', '0987', '2007-07-24', '10:00:00', 'Cluj'),
    ('Briana Gheorghe', 'brianaagheorghe@yahoo.com', 'bribri', '2004-01-03', '22:12:00', 'Sibiu'),
    ('sore marian', 'soremarian@gmail.com', 'sore1', '1990-09-10', '06:23:00', 'Victoria'),
    ('Diana', 'diana@yahoo.com', '12345', '2000-03-22', '10:26:00', 'Cluj'),
    ('ioana', 'ioana@gmail.com', 'parola1', '2004-06-25', '10:36:00', 'Fagaras');

-- Insert data into Admin table
INSERT INTO "Admin" (name, email, password)
VALUES
    ('Bogdan Popa', 'bogdan.popa@yahoo.com', '1234'),
    ('Ioana Popa', 'ioana.popa@yahoo.com', '1234');

-- Insert data into Quote table
INSERT INTO "Quote" (element, quoteText)
VALUES
    ('Fire', 'The only trip you will regret is the one you don’t take.'),
    ('Fire', 'Adventure is worthwhile in itself.'),
    ('Fire', 'Life begins at the end of your comfort zone.'),
    ('Fire', 'Free spirits dont ask for permission.'),
    ('Water', 'Normal is nothing more than a cycle on a washing machine.'),
    ('Water', 'The great gift of human beings is that we have the power of empathy.'),
    ('Water', 'To be rude to someone is not my nature.'),
    ('Water', 'Learn as much from joy as you do from pain.'),
    ('Air', 'That was her gift. She filled you with words you didn’t know were there.'),
    ('Air', 'I feel like Im too busy writing history to read it.'),
    ('Air', 'Identify with everything. Align with nothing.'),
    ('Air', 'Everything in the universe is within you. Ask all from yourself.'),
    ('Earth', 'Empty yourself and let the universe fill you.'),
    ('Earth', 'Fall seven times, stand up eight.'),
    ('Earth', 'I have standards I don’t plan on lowering for anybody, including myself.'),
    ('Earth', 'Be easily awed, not easily impressed.');

-- Insert data into Trait table
INSERT INTO "Trait" (element, traitName)
VALUES
    ('Fire', 'passionate'),
    ('Fire', 'playful'),
    ('Fire', 'energized'),
    ('Water', 'emotional'),
    ('Water', 'intuitive'),
    ('Water', 'nurturing'),
    ('Air', 'adventurous'),
    ('Air', 'curious'),
    ('Air', 'sociable'),
    ('Earth', 'stable'),
    ('Earth', 'pragmatic'),
    ('Earth', 'analytic');

-- Insert friend relationships into User_Friends table
INSERT INTO "User_Friends" (userId, friendId) VALUES
                                                  (1, 2), (1, 3), (1, 4), (1, 5), -- Amna's friends
                                                  (2, 1),                         -- Florian's friends
                                                  (3, 4),                         -- Briana's friends
                                                  (4, 1), (4, 3), (4, 5), (4, 6), -- sore marian's friends
                                                  (5, 1), (5, 4),                 -- Diana's friends
                                                  (6, 4);                         -- ioana's friends

-- Insert data into StarSign table
INSERT INTO "StarSign" (starName, element)
VALUES
    ('Aries', 'Fire'),
    ('Taurus', 'Earth'),
    ('Gemini', 'Air'),
    ('Cancer', 'Water'),
    ('Leo', 'Fire'),
    ('Virgo', 'Earth'),
    ('Libra', 'Air'),
    ('Scorpio', 'Water'),
    ('Sagittarius', 'Fire'),
    ('Capricorn', 'Earth'),
    ('Aquarius', 'Air'),
    ('Pisces', 'Water');

-- Insert data into StarSign_Trait table
-- Aries traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (1, 1), (1, 2), (1, 3);

-- Taurus traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (2, 10), (2, 11), (2, 12);

-- Gemini traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (3, 7), (3, 8), (3, 9);

-- Cancer traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (4, 4), (4, 5), (4, 6);

-- Leo traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (5, 1), (5, 2), (5, 3);

-- Virgo traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (6, 10), (6, 11), (6, 12);

-- Libra traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (7, 7), (7, 8), (7, 9);

-- Scorpio traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (8, 4), (8, 5), (8, 6);

-- Sagittarius traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (9, 1), (9, 2), (9, 3);

-- Capricorn traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (10, 10), (10, 11), (10, 12);

-- Aquarius traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (11, 7), (11, 8), (11, 9);

-- Pisces traits
INSERT INTO "StarSign_Trait" (starSignId, traitId) VALUES
                                                       (12, 4), (12, 5), (12, 6);
