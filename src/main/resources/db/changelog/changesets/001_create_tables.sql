CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE INDEX idx_user_email ON users (email);

CREATE TABLE tasks (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT NOT NULL,
                       status VARCHAR(50) NOT NULL,
                       priority VARCHAR(50) NOT NULL,
                       timestamp TIMESTAMP NOT NULL,
                       executor_id BIGINT,
                       owner_id BIGINT,
                       CONSTRAINT fk_executor FOREIGN KEY (executor_id) REFERENCES users (id) ON DELETE SET NULL,
                       CONSTRAINT fk_owner FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_owner_id ON tasks (owner_id);
CREATE INDEX idx_executor_id ON tasks (executor_id);

CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          text TEXT NOT NULL,
                          timestamp TIMESTAMP NOT NULL,
                          sender_id BIGINT,
                          task_id BIGINT,
                          CONSTRAINT fk_sender FOREIGN KEY (sender_id) REFERENCES users (id) ON DELETE CASCADE,
                          CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);
