-- reset database file

DROP TABLE IF EXISTS "Customer" CASCADE;
DROP TABLE IF EXISTS "Manager" CASCADE;
DROP TABLE IF EXISTS "Wallet" CASCADE;
DROP TABLE IF EXISTS "Species" CASCADE;
DROP TABLE IF EXISTS "Order" CASCADE;
DROP TABLE IF EXISTS "OrderStatus" CASCADE;
DROP TABLE IF EXISTS "OrderItems" CASCADE;
DROP TABLE IF EXISTS "Item" CASCADE;
DROP TABLE IF EXISTS "Category" CASCADE;
DROP TABLE IF EXISTS "Inventory" CASCADE;
DROP TABLE IF EXISTS "ArcaneRequest" CASCADE;
DROP TABLE IF EXISTS "RequestStatus" CASCADE;

CREATE TABLE "Manager" (
  managerID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  surname VARCHAR(50) NOT NULL,
  email VARCHAR(80) UNIQUE NOT NULL,
  password VARCHAR(50) NOT NULL 
  );

CREATE TABLE "Customer" (
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

CREATE TABLE "Wallet" (
  customerID INT PRIMARY KEY,
  CPbalance INT DEFAULT 0 CONSTRAINT positiveBalance CHECK (CPbalance >= 0),
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE "Species" (
  speciesID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  adultAge INT NOT NULL,
  limitAge INT NOT NULL
  );

CREATE TABLE "Order" (
  orderID SERIAL PRIMARY KEY,
  customerID INT,
  orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  orderStatus INT,
  totalCP INT NOT NULL
  );

CREATE TABLE IF NOT EXISTS "OrderStatus" (
  orderStatusID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
  );

CREATE TABLE "OrderItems" (
  orderID INT,
  itemID INT,
  quantity INT CONSTRAINT positiveQuantity CHECK (quantity > 0),
  PRIMARY KEY (orderID, itemID),
  FOREIGN KEY (orderID) REFERENCES "Order"(orderID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (itemID) REFERENCES "Item"(itemID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE "Item" (
  itemID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100),
  CPprice INT NOT NULL,
  categoryID INT,
  arcane BOOLEAN NOT NULL,
  FOREIGN KEY (categoryID) REFERENCES "Category"(categoryID) ON UPDATE CASCADE
  );

CREATE TABLE "Category" (
  categoryID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100)
  );

CREATE TABLE "Inventory" (
  customerID INT,
  itemID INT,
  quantity INT CONSTRAINT positiveInventory CHECK (quantity > 0),
  PRIMARY KEY (customerID, itemID),
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (itemID) REFERENCES "Item"(itemID) ON UPDATE CASCADE ON DELETE CASCADE
  );

CREATE TABLE "ArcaneRequest" (
  requestID SERIAL PRIMARY KEY,
  customerID INT,
  statusID INT,
  FOREIGN KEY (customerID) REFERENCES "Customer"(customerID) ON UPDATE CASCADE ON DELETE CASCADE,
  FOREIGN KEY (statusID) REFERENCES "RequestStatus"(statusID) ON UPDATE CASCADE
  );

CREATE TABLE "RequestStatus" (
  statusID SERIAL PRIMARY KEY,
  name VARCHAR(50) NOT NULL
  );
