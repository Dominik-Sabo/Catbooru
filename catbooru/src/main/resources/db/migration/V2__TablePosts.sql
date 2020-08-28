CREATE TABLE posts(
    id UUID NOT NULL PRIMARY KEY,
    userId UUID NOT NULL,
    filePath VARCHAR(100) NOT NULL,
    upvotes SMALLINT DEFAULT 0,
    dateUploaded TIMESTAMP NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id)
)