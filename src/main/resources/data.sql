-- Insert dummy data
-- Users
INSERT INTO _users (last_connectivity, email, first_name, last_name, password) VALUES ('2023-06-07', 'john@example.com', 'John', 'Doe', 'password1');
INSERT INTO _users (last_connectivity, email, first_name, last_name, password) VALUES ('2023-06-07', 'alice@example.com', 'Alice', 'Smith', 'password2');
INSERT INTO _users (last_connectivity, email, first_name, last_name, password) VALUES ('2023-06-07', 'bob@example.com', 'Bob', 'Johnson', 'password3');

-- Topics
INSERT INTO topic (description, title) VALUES ('Technology-related discussions', 'Technology');
INSERT INTO topic (description, title) VALUES ('Sports-related discussions', 'Sports');
INSERT INTO topic (description, title) VALUES ('Movie-related discussions', 'Movies');

-- Posts
INSERT INTO post (last_updated, content, title) VALUES ('2023-06-07', 'Lorem ipsum dolor sit amet.', 'Post 1');
INSERT INTO post (last_updated, content, title) VALUES ('2023-06-08', 'Consectetur adipiscing elit.', 'Post 2');
INSERT INTO post (last_updated, content, title) VALUES ('2023-06-09', 'Sed do eiusmod tempor incididunt.', 'Post 3');

-- Post-Topic relationships
INSERT INTO topics_posts (post_id, topic_id) VALUES (1, 1);
INSERT INTO topics_posts (post_id, topic_id) VALUES (1, 2);
INSERT INTO topics_posts (post_id, topic_id) VALUES (2, 1);
INSERT INTO topics_posts (post_id, topic_id) VALUES (3, 3);

-- User-Topic relationships
INSERT INTO topics_users (topic_id, user_id) VALUES (1, 1);
INSERT INTO topics_users (topic_id, user_id) VALUES (2, 2);
INSERT INTO topics_users (topic_id, user_id) VALUES (1, 3);
INSERT INTO topics_users (topic_id, user_id) VALUES (3, 3);

-- Likes
INSERT INTO likes (when_liked, post_id, user_id) VALUES ('2023-06-08', 1, 2);
INSERT INTO likes (when_liked, post_id, user_id) VALUES ('2023-06-09', 2, 3);
INSERT INTO likes (when_liked, post_id, user_id) VALUES ('2023-06-10', 3, 1);
