
USE crisis_management_db;

CREATE TABLE IF NOT EXISTS allocations (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity_allocated INT NOT NULL,
    allocated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_allocation_quantity
        CHECK (quantity_allocated > 0),

    CONSTRAINT fk_allocation_request
        FOREIGN KEY (request_id)
        REFERENCES relief_requests(request_id),

    CONSTRAINT fk_allocation_item
        FOREIGN KEY (item_id)
        REFERENCES inventory_items(item_id)
);
