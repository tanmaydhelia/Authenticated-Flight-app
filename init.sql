-- 1. Create Databases
CREATE DATABASE IF NOT EXISTS flight_service_DB;
CREATE DATABASE IF NOT EXISTS booking_DB;
CREATE DATABASE IF NOT EXISTS identity_db;

-- 2. Grant Permissions
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE flight_service_DB;

-- Create airline table
CREATE TABLE IF NOT EXISTS airline (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL
);

-- Insert Airlines
INSERT INTO airline (name, code) VALUES 
('IndiGo', '6E'),
('Air India', 'AI'),
('Vistara', 'UK'),
('SpiceJet', 'SG'),
('Emirates', 'EK');

-- Create flight table 
-- (Note: 'availabe_seats' matches the typo in your backend DTOs)
CREATE TABLE IF NOT EXISTS flight (
    id INT AUTO_INCREMENT PRIMARY KEY,
    airline_id INT,
    from_airport VARCHAR(100),
    to_airport VARCHAR(100),
    departure_time DATETIME,
    arrival_time DATETIME,
    price DOUBLE,
    total_seats INT,
    availabe_seats INT,
    FOREIGN KEY (airline_id) REFERENCES airline(id)
);
