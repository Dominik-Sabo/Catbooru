CREATE TABLE upvotes(
    userId UUID NOT NULL,
    postId UUID NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (postId) REFERENCES posts(id)
)