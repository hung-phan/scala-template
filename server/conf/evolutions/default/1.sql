# --- !Ups
CREATE TABLE IF NOT EXISTS todos (
  id       SERIAL PRIMARY KEY,
  text     TEXT NOT NULL,
  complete BOOLEAN DEFAULT FALSE
);

# --- !Downs
DROP TABLE IF EXISTS todos;
