DROP TABLE IF EXISTS hits CASCADE;

CREATE TABLE IF NOT EXISTS hits(
    id INTEGER GENERATED ALWAYS AS IDENTITY NOT NULL,
    app VARCHAR(255),
    uri VARCHAR(255),
    ip VARCHAR(16),
    timestamp TIMESTAMP,
    CONSTRAINT pk_hits PRIMARY KEY (id)
);