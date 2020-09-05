CREATE TABLE comments(
    id UUID NOT NULL PRIMARY KEY,
    userId UUID NOT NULL,
    postId UUID NOT NULL,
    commentText VARCHAR(500) NOT NULL,
    timeCommented TIMESTAMP NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (postId) REFERENCES posts(id)
)