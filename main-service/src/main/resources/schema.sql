DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS event_likes CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    name VARCHAR(50),
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    lat FLOAT,
    lon FLOAT,
    CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    annotation VARCHAR(2000),
    category_id BIGINT,
    confirmed_requests INTEGER,
    created_on TIMESTAMP,
    description VARCHAR(7000),
    event_date TIMESTAMP,
    initiator BIGINT,
    location_id BIGINT,
    paid BOOLEAN,
    participant_limit INTEGER,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR(20),
    title VARCHAR(120),
    views INTEGER,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_events_category_id FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT fk_events_initiator FOREIGN KEY (initiator) REFERENCES users (id),
    CONSTRAINT fk_events_location_id FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    created TIMESTAMP,
    event_id BIGINT,
    requester_id BIGINT,
    status VARCHAR(20),
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_requests_requester_id FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT uq_requests_req_id_event_id UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    pinned BOOLEAN,
    title VARCHAR(50),
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_event (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT fk_ce_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fk_ce_event_id FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS event_likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    user_id BIGINT,
    event_id BIGINT,
    int_like INTEGER,
    CONSTRAINT pk_event_rating PRIMARY KEY (id),
    CONSTRAINT fk_el_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_el_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT uq_el_user_id_event_id UNIQUE (event_id, user_id)
);