USE campus_facility_db;

-- Net total = subtotal, minus 15% when the role is FACULTY
INSERT INTO reservations
(user_name, user_role, item_category, quantity, booking_days, daily_rate, subtotal, net_total) VALUES
('AHMED ALI',      'STUDENT', 'LAPTOP',    1, 3, 50.00,   150.00,  150.00),
('FATIMA HASSAN',  'STUDENT', 'POD',       1, 2, 100.00,  200.00,  200.00),
('DR. OMAR KHALID','FACULTY', 'PROJECTOR', 2, 5, 80.00,   800.00,  680.00),
('DR. LAYLA NOOR', 'FACULTY', 'LAPTOP',    10, 4, 50.00, 2000.00, 1700.00),
('SARA MOHAMMED',  'STUDENT', 'PROJECTOR', 1, 1, 80.00,   80.00,   80.00);
