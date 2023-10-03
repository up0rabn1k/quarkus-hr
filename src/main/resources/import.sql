INSERT INTO departments (created_at, id, updated_at, version, name)
VALUES (N'2023-10-03 21:18:00.097000 +00:00', 1, N'2023-10-03 21:18:04.742000 +00:00', 0, N'IT');
INSERT INTO departments (created_at, id, updated_at, version, name)
VALUES (N'2023-10-03 21:18:21.617000 +00:00', 2, N'2023-10-03 21:18:25.628000 +00:00', 0, N'MA');
INSERT INTO departments (created_at, id, updated_at, version, name)
VALUES (N'2023-10-03 21:18:36.268000 +00:00', 3, N'2023-10-03 21:18:39.934000 +00:00', 0, N'SA');

INSERT INTO employees (created_at, department_id, id, updated_at, version, location, name)
VALUES (N'2023-10-03 21:19:43.982000 +00:00', 1, 1, N'2023-10-03 21:19:49.595000 +00:00', 0, N'GB',
        N'firstName19119 lastName19119');
INSERT INTO employees (created_at, department_id, id, updated_at, version, location, name)
VALUES (N'2023-10-03 21:20:09.899000 +00:00', 1, 2, N'2023-10-03 21:20:16.224000 +00:00', 0, N'US',
        N'firstName2 lastName2');


