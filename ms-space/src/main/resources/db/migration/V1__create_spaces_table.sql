CREATE TABLE tb_spaces (
    id BIGINT NOT NULL AUTO_INCREMENT,
    owner_name VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    area_square_meters DECIMAL(10, 2) NOT NULL,
    residence_count INT NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id)
);