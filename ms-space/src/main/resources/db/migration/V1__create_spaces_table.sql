CREATE TABLE tb_spaces (
    id BIGINT NOT NULL AUTO_INCREMENT,
    owner_id VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    neighborhood VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    area_square_meters DECIMAL(10, 2) NOT NULL,
    monthly_rent DECIMAL(10, 2) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    image_url VARCHAR(255),
    contact_name VARCHAR(255) NOT NULL,
    contact_email VARCHAR(255) NOT NULL,
    residence_count INT NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id)
);