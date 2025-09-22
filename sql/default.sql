-- Data Manager
INSERT INTO "Manager" (name, surname, email, password)
VALUES
    ('Alice', 'Smith', 'alice.smith@gmail.com', 'password123'),
    ('Bob', 'Johnson', 'bob.johnson@gmail.com', 'securepass456');

-- Data Species
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

-- Data Customer
INSERT INTO "Customer" (name, surname, email, password, age, arcaneMembership, phone, speciesid)
VALUES
    ('John', 'Doe', 'john.doe@gmail.com', 'mypassword789', 30, FALSE, '1234567890', 1),
    ('Jane', 'Roe', 'jane.roe@gmail.com', 'mypassword123', 25, FALSE, '0987654321', 2),
    ('Emily', 'Davis', 'emily.davis@gmail.com', 'strongpassword456', 40, FALSE, '1122334455', 3);

-- Data Wallet
INSERT INTO "Wallet" (customerID, CPbalance)
VALUES
    (1, 1000),
    (2, 500),
    (3, 1200);

-- Data OrderStatus
INSERT INTO "OrderStatus" (name)
VALUES
    ('In progress'),
    ('Shipping'),
    ('Delivered');

-- Data Category
INSERT INTO "Category" (name, description)
VALUES
    ('Weapons', 'Items related to combat and defense'),
    ('Armor', 'Protective gear for the body'),
    ('Potions', 'Items that have magical effects');

-- Data Item
INSERT INTO "Item" (name, description, CPprice, categoryID, arcane, imagepath)
VALUES
    ('Sword of Flames', 'A sword imbued with the power of fire', 500, 1, FALSE, '/images/products/fire_sword.png'),
    ('Dragon Shield', 'A shield made from dragon scales', 300, 2, FALSE, '/images/products/dragon_shield.png'),
    ('Healing Potion', 'A potion that restores health', 50, 3, FALSE, '/images/products/healing_potion.png');

-- Data Order
INSERT INTO "Order" (customerID, orderDate, statusid, totalCP)
VALUES
    (1, '2025-03-01 10:00:00', 1, 800),
    (2, '2025-03-02 15:30:00', 2, 100),
    (3, '2025-03-03 20:45:00', 3, 500);

-- Data OrderItems
INSERT INTO "OrderItems" (orderID, itemID, quantity)
VALUES
    (1, 1, 1),  -- Order 1: Sword of Flames
    (1, 2, 1),  -- Order 1: Dragon Shield
    (2, 3, 2),  -- Order 2: Healing Potion
    (3, 1, 1);  -- Order 3: Sword of Flames

-- Data Inventory
INSERT INTO "Inventory" (customerID, itemID, quantity)
VALUES
    (1, 1, 1),  -- Customer 1: Sword of Flames
    (1, 2, 1),  -- Customer 1: Dragon Shield
    (2, 3, 2),  -- Customer 2: Healing Potions
    (3, 1, 1);  -- Customer 3: Dragon Shield

-- Data RequestStatus
INSERT INTO "RequestStatus" (name)
VALUES
    ('Pending'),
    ('Approved'),
    ('Rejected');

-- Data ArcaneRequest
INSERT INTO "ArcaneRequest" (customerID, statusID)
VALUES
    (1, 1),  -- Customer 1: In Progress
    (2, 2),  -- Customer 2: Completed
    (3, 3);  -- Customer 3: Rejected
