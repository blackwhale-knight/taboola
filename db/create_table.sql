CREATE TABLE Events (
    id  int  IDENTITY NOT NULL,
    event_id int NOT NULL,
    time_bucket timestamp NOT NULL,
    count int NOT NULL,
    PRIMARY KEY (id)
);