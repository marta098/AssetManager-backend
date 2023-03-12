SET IDENTITY_INSERT role ON;
INSERT INTO role (id, name)
VALUES (0, 'MANAGER_IT'),
       (1, 'EMPLOYEE_IT'),
       (2, 'MANAGER_DHL'),
       (3, 'EMPLOYEE_DHL');
SET IDENTITY_INSERT role OFF;

INSERT INTO [user] (username, email, password, role_id)
VALUES ('ITmanager1', 'ITmanager1@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 0),
       ('ITmanager2', 'ITmanager2@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 0),
       ('ITemployee1', 'ITemployee1@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 1),
       ('ITemployee2', 'ITemployee2@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 1),
       ('ITemployee3', 'ITemployee3@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 1),
       ('DHLmanager1', 'DHLmanager1@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 2),
       ('DHLemployee1', 'DHLemployee1@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 3),
       ('DHLemployee2', 'DHLemployee2@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 3),
       ('DHLemployee3', 'DHLemployee3@dhl.com', '$2a$10$aYZVX/tu3WrsaI2MnEBAF.woMZY/sIbcyU/2GHDCep1oIZHHUaAZe', 3);

INSERT INTO asset (crest_number, model, serial_number, type, warranty_expiration)
VALUES ('MPK620040', 'HP Elitebook 840 G6', '123-456-test1', 'LAPTOP', '2023-01-01 08:00:00.0000000'),
       ('MPK120525', 'HP Elitebook 840 G3', '123-456-test2', 'LAPTOP', '2023-02-01 08:00:00.0000000'),
       ('MPK110600', 'HP Elitebook 840 G5', '123-456-test3', 'LAPTOP', '2022-06-01 10:00:00.0000000');

INSERT INTO report (from_date, link, timestamp, to_date, generated_by_id)
VALUES ('2022-01-26 08:00:00.0000000', 'https://www.mocked-report.com/1', '2022-02-01 08:45:12.0000000',
        '2022-02-01 08:00:00.0000000', 3),
       ('2022-01-28 08:00:00.0000000', 'https://www.mocked-report.com/2', '2022-02-04 10:25:30.0000000',
        '2022-02-04 08:00:00.0000000', 4);

INSERT INTO [order] (assignment_date, delivery_type, pickup_date, remark, status, asset_id, assigned_by_id,
                     assigned_to_id)
VALUES ('2022-03-30 13:20:00.0000000', 'SHIPMENT', '2022-04-10 08:00:00.0000000', 'dysk min 500GB', 'IN_PREPARATION', 1, 3, 7),
       ('2022-02-02 10:30:00.0000000', 'PICKUP', '2022-02-06 12:35:00.0000000', null, 'COMPLETED', 2, 4, 8);

INSERT INTO change_history (remark, status, timestamp, author_id, order_id)
VALUES (null, 'NEW', '2022-03-30 13:20:00.0000000', 7, 1),
       ('dysk min 500GB', 'NEW', '2022-03-30 15:20:00.0000000', 7, 1),
       (null, 'HANDED_FOR_COMPLETION', '2022-04-01 09:21:34.0000000', 1, 1),
       (null, 'ALLOCATED', '2022-04-01 10:11:35.0000000', 3, 1),
       (null, 'NEW', '2022-02-02 10:30:00.0000000', 8, 2),
       (null, 'HANDED_FOR_COMPLETION', '2022-02-03 12:00:00.0000000', 4, 2),
       (null, 'ALLOCATED', '2022-01-04 10:00:00.0000000', 4, 2),
       (null, 'IN_PREPARATION', '2022-01-04 13:00:00.0000000', 4, 2),
       (null, 'SENT', '2022-01-05 10:00:00.0000000', 4, 2);