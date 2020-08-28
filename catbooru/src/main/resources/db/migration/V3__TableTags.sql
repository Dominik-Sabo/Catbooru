CREATE TABLE tags(
    tag VARCHAR(30) NOT NULL,
    postId UUID NOT NULL,
    FOREIGN KEY (postId) REFERENCES posts(id)
)