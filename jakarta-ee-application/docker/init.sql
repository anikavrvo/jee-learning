CREATE TABLE IF NOT EXISTS members (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(50),
    email_address VARCHAR(100),
    cell_number VARCHAR(10)
);

INSERT INTO members (full_name, email_address, cell_number) VALUES
    ('Alpha', 'alpha@example.com', '0832013357'),
    ('Beta', 'beta@example.com', '0832122332'),
    ('Gamma', 'gamma@example.com', '0732303000');