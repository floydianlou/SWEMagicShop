-- Dati per la tabella Manager
INSERT INTO "Manager" (name, surname, email, password)
VALUES
    ('Alice', 'Smith', 'alice.smith@gmail.com', 'password123'),
    ('Bob', 'Johnson', 'bob.johnson@gmail.com', 'securepass456');

-- Dati per la tabella Species
INSERT INTO "Species" (name, adultAge, limitAge)
VALUES
    ('Human', 18, 100),
    ('Elf', 100, 800),
    ('Half Elf', 18, 180),
    ('Dragon Born', 15, 80),
    ('Gnome', 18, 500),
    ('Halfling', 20, 250),
    ('Half Orc', 14, 75),
    ('Tiefling', 18, 120),
    ('Dwarf', 30, 350);

-- Dati per la tabella Customer
INSERT INTO "Customer" (name, surname, email, password, age, arcaneMembership, phone, speciesID)
VALUES
    ('John', 'Doe', 'john.doe@gmail.com', 'mypassword789', 30, TRUE, '1234567890', 1),
    ('Jane', 'Roe', 'jane.roe@gmail.com', 'mypassword123', 25, FALSE, '0987654321', 2),
    ('Emily', 'Davis', 'emily.davis@gmail.com', 'strongpassword456', 40, TRUE, '1122334455', 3);

-- Dati per la tabella Wallet
INSERT INTO "Wallet" (customerID, CPbalance)
VALUES
    (1, 1000),
    (2, 500),
    (3, 1200);

-- Dati per la tabella OrderStatus
INSERT INTO "OrderStatus" (name)
VALUES
    ('Pending'),
    ('Delivered'),
    ('Cancelled');

-- Dati per la tabella Category
INSERT INTO "Category" (name, description)
VALUES
    ('Weapons', 'Items related to combat and defense'),
    ('Armor', 'Protective gear for the body'),
    ('Potions', 'Items that have magical effects');

-- Dati per la tabella Item
INSERT INTO "Item" (name, description, CPprice, categoryID, arcane)
VALUES
    ('Sword of Flames', 'A sword imbued with the power of fire', 500, 1, TRUE),
    ('Dragon Shield', 'A shield made from dragon scales', 300, 2, FALSE),
    ('Healing Potion', 'A potion that restores health', 50, 3, TRUE);

-- Dati per la tabella Order
INSERT INTO "Order" (customerID, orderDate, statusid, totalCP)
VALUES
    (1, '2025-03-01 10:00:00', 1, 550),
    (2, '2025-03-02 15:30:00', 2, 350),
    (3, '2025-03-03 20:45:00', 3, 120);

-- Dati per la tabella OrderItems
INSERT INTO "OrderItems" (orderID, itemID, quantity)
VALUES
    (1, 1, 1),  -- Order 1: Sword of Flames
    (1, 2, 1),  -- Order 1: Dragon Shield
    (2, 3, 2),  -- Order 2: Healing Potion
    (3, 1, 1);  -- Order 3: Sword of Flames

-- Dati per la tabella Inventory
INSERT INTO "Inventory" (customerID, itemID, quantity)
VALUES
    (1, 1, 1),  -- Customer 1: Sword of Flames
    (2, 3, 5),  -- Customer 2: Healing Potions
    (3, 2, 3);  -- Customer 3: Dragon Shield

-- Dati per la tabella RequestStatus
INSERT INTO "RequestStatus" (name)
VALUES
    ('In Progress'),
    ('Completed'),
    ('Rejected');

-- Dati per la tabella ArcaneRequest
INSERT INTO "ArcaneRequest" (customerID, statusID)
VALUES
    (1, 1),  -- Customer 1: New request
    (2, 2),  -- Customer 2: In Progress
    (3, 3);  -- Customer 3: Completed
