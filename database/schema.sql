
-- =====================================================
-- CRISIS MANAGEMENT AND RELIEF DISTRIBUTION SYSTEM
-- Database: MySQL 8.0
-- Version 1
-- =====================================================

CREATE DATABASE IF NOT EXISTS crisis_management_db;
USE crisis_management_db;


-- =====================================================
-- TABLE 1: VOLUNTEERS
-- Stores volunteer information
-- =====================================================

CREATE TABLE IF NOT EXISTS volunteers (
    volunteer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    skill VARCHAR(100),
    availability ENUM('Available', 'Assigned', 'Unavailable')
        NOT NULL DEFAULT 'Available',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- TABLE 2: RELIEF REQUESTS
-- Stores emergency relief requests
-- =====================================================

CREATE TABLE IF NOT EXISTS relief_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    requester_name VARCHAR(100) NOT NULL,
    requester_contact VARCHAR(15),
    location VARCHAR(200) NOT NULL,
    emergency_type ENUM(
        'Flood', 'Earthquake', 'Fire',
        'Cyclone', 'Landslide', 'Other'
    ) NOT NULL,
    description TEXT,
    priority ENUM('Low', 'Medium', 'High', 'Critical')
        NOT NULL DEFAULT 'Medium',
    status ENUM(
        'Pending', 'Approved', 'In Progress',
        'Fulfilled', 'Cancelled'
    ) NOT NULL DEFAULT 'Pending',
    request_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- =====================================================
-- TABLE 3: INVENTORY ITEMS
-- Stores available relief supplies
-- =====================================================

CREATE TABLE IF NOT EXISTS inventory_items (
    item_id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL UNIQUE,
    category ENUM(
        'Food', 'Water', 'Medicine',
        'Clothing', 'Shelter', 'Other'
    ) NOT NULL,
    unit VARCHAR(30) NOT NULL,
    quantity_available INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_inventory_quantity
        CHECK (quantity_available >= 0),

    CONSTRAINT chk_reorder_level
        CHECK (reorder_level >= 0)
);


-- =====================================================
-- TABLE 4: REQUEST ITEMS
-- Items and quantities needed for each request
-- =====================================================

CREATE TABLE IF NOT EXISTS request_items (
    request_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity_needed INT NOT NULL,

    PRIMARY KEY (request_id, item_id),

    CONSTRAINT chk_quantity_needed
        CHECK (quantity_needed > 0),

    FOREIGN KEY (request_id)
        REFERENCES relief_requests(request_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (item_id)
        REFERENCES inventory_items(item_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- =====================================================
-- TABLE 5: DISPATCHES
-- Tracks deliveries and assigned volunteers
-- =====================================================

CREATE TABLE IF NOT EXISTS dispatches (
    dispatch_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    volunteer_id INT NOT NULL,
    status ENUM(
        'Planned', 'Dispatched',
        'Delivered', 'Cancelled'
    ) NOT NULL DEFAULT 'Planned',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dispatched_at DATETIME NULL,
    delivered_at DATETIME NULL,
    notes VARCHAR(500),

    FOREIGN KEY (request_id)
        REFERENCES relief_requests(request_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (volunteer_id)
        REFERENCES volunteers(volunteer_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- =====================================================
-- TABLE 6: DISPATCH ITEMS
-- Supplies included in each dispatch
-- =====================================================

CREATE TABLE IF NOT EXISTS dispatch_items (
    dispatch_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,

    PRIMARY KEY (dispatch_id, item_id),

    CONSTRAINT chk_dispatch_quantity
        CHECK (quantity > 0),

    FOREIGN KEY (dispatch_id)
        REFERENCES dispatches(dispatch_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (item_id)
        REFERENCES inventory_items(item_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


-- =====================================================
-- SAMPLE DATA
-- INSERT IGNORE avoids duplicate-key errors when
-- this sample section is executed more than once.
-- =====================================================

INSERT IGNORE INTO volunteers
    (volunteer_id, full_name, phone, email, skill, availability)
VALUES
    (1, 'Aarav Sharma', '9000000001',
     'aarav@example.com', 'First Aid', 'Available'),

    (2, 'Priya Verma', '9000000002',
     'priya@example.com', 'Logistics', 'Available'),

    (3, 'Rahul Singh', '9000000003',
     'rahul@example.com', 'Rescue Support', 'Available');


INSERT IGNORE INTO inventory_items
    (item_id, item_name, category, unit,
     quantity_available, reorder_level)
VALUES
    (1, 'Food Packets', 'Food', 'packets', 200, 50),
    (2, 'Drinking Water', 'Water', 'bottles', 500, 100),
    (3, 'First Aid Kits', 'Medicine', 'kits', 40, 10),
    (4, 'Blankets', 'Shelter', 'pieces', 80, 20),
    (5, 'Clothing Kits', 'Clothing', 'kits', 60, 15);


INSERT IGNORE INTO relief_requests
    (request_id, requester_name, requester_contact,
     location, emergency_type, description, priority, status)
VALUES
    (1, 'Anita Gupta', '9111111111',
     'Green Valley', 'Flood',
     'Food and water required for affected families.',
     'Critical', 'Pending'),

    (2, 'Rohit Mehta', '9222222222',
     'Hill Town', 'Landslide',
     'Blankets and first aid supplies required.',
     'High', 'Pending'),

    (3, 'Neha Patel', '9333333333',
     'Central District', 'Fire',
     'Emergency food supplies requested.',
     'Medium', 'Approved');


INSERT IGNORE INTO request_items
    (request_id, item_id, quantity_needed)
VALUES
    (1, 1, 100),
    (1, 2, 150),
    (2, 3, 10),
    (2, 4, 30),
    (3, 1, 40);


-- =====================================================
-- VIEW: REQUEST SUMMARY
-- Shows each request with the number of requested items
-- =====================================================

CREATE OR REPLACE VIEW vw_request_summary AS
SELECT
    r.request_id,
    r.requester_name,
    r.location,
    r.emergency_type,
    r.priority,
    r.status,
    COUNT(ri.item_id) AS different_items_requested
FROM relief_requests r
LEFT JOIN request_items ri
    ON r.request_id = ri.request_id
GROUP BY
    r.request_id,
    r.requester_name,
    r.location,
    r.emergency_type,
    r.priority,
    r.status;


-- =====================================================
-- TEST QUERIES AND REPORTS
-- Run these after the setup completes
-- =====================================================

-- 1. Check all tables
SHOW TABLES;

-- 2. View all volunteers
SELECT * FROM volunteers;

-- 3. View all inventory
SELECT * FROM inventory_items;

-- 4. View relief requests
SELECT * FROM relief_requests;

-- 5. View requested items with their names
SELECT
    r.request_id,
    r.location,
    i.item_name,
    ri.quantity_needed
FROM request_items ri
JOIN relief_requests r
    ON ri.request_id = r.request_id
JOIN inventory_items i
    ON ri.item_id = i.item_id
ORDER BY r.request_id;

-- 6. View pending and critical requests first
SELECT *
FROM relief_requests
WHERE status IN ('Pending', 'Approved')
ORDER BY
    CASE priority
        WHEN 'Critical' THEN 1
        WHEN 'High' THEN 2
        WHEN 'Medium' THEN 3
        WHEN 'Low' THEN 4
    END;

-- 7. Check inventory that has reached its reorder level
SELECT item_name, quantity_available, reorder_level
FROM inventory_items
WHERE quantity_available <= reorder_level;

-- 8. View the request summary
SELECT * FROM vw_request_summary;

-- 9. View dispatch details
SELECT
    d.dispatch_id,
    r.location,
    v.full_name AS volunteer_name,
    d.status,
    d.created_at
FROM dispatches d
JOIN relief_requests r
    ON d.request_id = r.request_id
JOIN volunteers v
    ON d.volunteer_id = v.volunteer_id;

-- 10. Count requests by status
SELECT status, COUNT(*) AS total_requests
FROM relief_requests
GROUP BY status;