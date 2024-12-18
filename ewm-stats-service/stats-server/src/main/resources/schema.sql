DROP TABLE IF EXISTS stats;

CREATE TABLE IF NOT EXISTS stats (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app	VARCHAR(8000) NOT NULL,
    ip	VARCHAR(39) NOT NULL,
    time_stamp TIMESTAMP NOT NULL,
    uri	VARCHAR(8000) NOT NULL,
    CONSTRAINT pk_stats PRIMARY KEY (id)
);