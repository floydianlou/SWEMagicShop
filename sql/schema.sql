-- database structure

CREATE TABLE IF NOT EXISTS "Manager" (
  managerID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  email VARCHAR(80) UNIQUE NOT NULL,
  password VARCHAR(50) NOT NULL 
  );

CREATE TABLE IF NOT EXISTS "Customer" (
  customerID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  email VARCHAR(80) UNIQUE NOT NULL,
  password VARCHAR(50) NOT NULL,
  age INTEGER CONSTRAINT agePositive CHECK (age >= 0),
  arcaneMembership BOOLEAN DEFAULT FALSE,
  phone VARCHAR(10) UNIQUE,
  speciesID INT,
  FOREIGN KEY (speciesID) REFERENCES "Species"(speciesID) ON UPDATE CASCADE
  );

CREATE TABLE IF NOT EXISTS "Wallet" (
  customerID INT PRIMARY KEY,
  CPbalance INT DEFAULT 0 CONSTRAINT positiveBalance CHECK (CPbalance >= 0),
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS "Species" (
  speciesID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  adultAge INT NOT NULL,
  limitAge INT NOT NULL
  );

CREATE TABLE IF NOT EXISTS "Order" (
  orderID SERIAL PRIMARY KEY,
  customerID INT,
  orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  orderStatus INT,
  totalCP INT NOT NULL,
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS "OrderStatus" (
  orderStatusID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
  );

CREATE TABLE IF NOT EXISTS "OrderItems" (
  orderID INT,
  itemID INT,
  quantity INT CONSTRAINT positiveQuantity CHECK (quantity > 0),
  PRIMARY KEY (orderID, itemID),
  FOREIGN KEY (orderID) REFERENCES "Order"(orderID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (itemID) REFERENCES "Item"(itemID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS "Item" (
  itemID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100),
  CPprice INT NOT NULL,
  categoryID INT,
  arcane BOOLEAN NOT NULL,
  FOREIGN KEY (categoryID) REFERENCES "Category"(categoryID) ON UPDATE CASCADE
  );

CREATE TABLE IF NOT EXISTS "Category" (
  categoryID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100)
  );

CREATE TABLE IF NOT EXISTS "Inventory" (
  customerID INT,
  itemID INT,
  quantity INT CONSTRAINT positiveInventory CHECK (quantity > 0),
  PRIMARY KEY (customerID, itemID),
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (itemID) REFERENCES "Item"(itemID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE IF NOT EXISTS "ArcaneRequest" (
  requestID SERIAL PRIMARY KEY,
  customerID INT,
  statusID INT,
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (statusID) REFERENCES "RequestStatus"(statusID) ON UPDATE CASCADE
  );

CREATE TABLE IF NOT EXISTS "RequestStatus" (
  statusID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
  );
