CREATE TABLE player(
    uuid UUID NOT NULL PRIMARY KEY,
    login_name VARCHAR NOT NULL,
    display_name VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    rank INTEGER NOT NULL,
    banned_until TIMESTAMP,
    banned BOOLEAN NOT NULL
);