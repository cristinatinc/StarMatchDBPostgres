CREATE TABLE "User" (
                        id SERIAL PRIMARY KEY,
                        name TEXT NOT NULL,
                        birthDate DATE,
                        birthTime TIME,
                        birthPlace TEXT,
                        email TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
);

CREATE TABLE "Quote" (
                       id SERIAL PRIMARY KEY,         -- Auto-incrementing unique identifier for the quote
                       element VARCHAR(50) NOT NULL,  -- Element associated with the quote, assuming a string representation
                       quoteText TEXT NOT NULL       -- Text of the quote
);

-- Table for Admin
CREATE TABLE "Admin" (
                       id SERIAL PRIMARY KEY,
                       name TEXT NOT NULL,
                       email TEXT UNIQUE NOT NULL,
                       password TEXT NOT NULL
);

-- Table for User_Friends (for managing friends as a many-to-many relationship)
CREATE TABLE "User_Friends" (
                              userId INTEGER NOT NULL,
                              friendId INTEGER NOT NULL,
                              PRIMARY KEY (userId, friendId),
                              FOREIGN KEY (userId) REFERENCES "User" (id) ON DELETE CASCADE,
                              FOREIGN KEY (friendId) REFERENCES "User" (id) ON DELETE CASCADE
);

-- Table for Traits
CREATE TABLE IF NOT EXISTS "Trait" (
                                       element TEXT NOT NULL,
                                       traitName TEXT NOT NULL,
                                       id SERIAL PRIMARY KEY

);

-- Table for Star Signs
CREATE TABLE IF NOT EXISTS "StarSign" (
                                        starName TEXT NOT NULL,
                                        element TEXT NOT NULL,
                                        id SERIAL PRIMARY KEY
);

-- Table for linking StarSigns and Traits (Many-to-Many relationship)
CREATE TABLE IF NOT EXISTS "StarSign_Trait" (
                                              starSignId INTEGER NOT NULL,        -- Foreign key reference to StarSign table
                                              traitId INTEGER NOT NULL,            -- Foreign key reference to Trait table
                                              PRIMARY KEY (starSignId, traitId),
                                              FOREIGN KEY (starSignId) REFERENCES "StarSign" (id) ON DELETE CASCADE,
                                              FOREIGN KEY (traitId) REFERENCES "Trait" (id) ON DELETE CASCADE
);