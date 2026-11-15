-- INT322 Group Assignment 1: Campus Facility & Equipment Reservation System
CREATE DATABASE IF NOT EXISTS campus_facility_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_facility_db;

DROP TABLE IF EXISTS reservations;

CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name      VARCHAR(100)  NOT NULL,
    user_role      ENUM('STUDENT','FACULTY')            NOT NULL,
    item_category  ENUM('LAPTOP','PROJECTOR','POD')     NOT NULL,
    quantity       INT           NOT NULL CHECK (quantity > 0),
    booking_days   INT           NOT NULL CHECK (booking_days > 0),
    daily_rate     DECIMAL(10,2) NOT NULL,
    subtotal       DECIMAL(10,2) NOT NULL,
    net_total      DECIMAL(10,2) NOT NULL,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
