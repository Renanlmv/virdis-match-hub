CREATE TABLE tb_cultivation_projects (
    id BIGINT NOT NULL AUTO_INCREMENT,
    space_id BIGINT NOT NULL,
    producer_name VARCHAR(255) NOT NULL,
    producer_email VARCHAR(255) NOT NULL,
    cultivation_type VARCHAR(255) NOT NULL,
    proposal_description VARCHAR(1000) NOT NULL,
    estimated_capacity INT NOT NULL,
    offered_monthly_rent DECIMAL(10, 2) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    PRIMARY KEY (id)
);