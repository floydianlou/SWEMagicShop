-- database structure

CREATE TABLE IF NOT EXISTS "Manager" (
  managerID SERIAL PRIMARY KEY,
  name VARCHAR(50),
  surname VARCHAR(50),
  email VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(50) NOT NULL 
  );
