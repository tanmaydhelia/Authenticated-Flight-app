-- 1. Create Databases
CREATE DATABASE IF NOT EXISTS flight_service_DB;
CREATE DATABASE IF NOT EXISTS booking_DB;
CREATE DATABASE IF NOT EXISTS identity_db;

-- 2. Grant Permissions
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;

-- 3. Select the Flight Service Database
USE flight_service_DB;

-- 4. Create the 'airline' table manually 
-- (Required because this runs before Hibernate)
CREATE TABLE IF NOT EXISTS airline (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL
);

-- 5. Insert Initial Airline Data
INSERT INTO airline (name, code) VALUES 
('IndiGo', '6E'),
('Air India', 'AI'),
('Vistara', 'UK'),
('SpiceJet', 'SG'),
('Emirates', 'EK'),
('Lufthansa', 'LH'),
('British Airways', 'BA'),
('Qatar Airways', 'QR');