CREATE TABLE location
(
    id   bigint IDENTITY (1, 1) NOT NULL,
    name varchar(255),
    CONSTRAINT pk_location PRIMARY KEY (id)
)
GO

CREATE TABLE stockroom
(
    id   bigint IDENTITY (1, 1) NOT NULL,
    name varchar(255),
    CONSTRAINT pk_stockroom PRIMARY KEY (id)
)
GO

ALTER TABLE asset
    ADD location_id bigint
GO

ALTER TABLE asset
    ADD stockroom_id bigint
GO

ALTER TABLE asset
    ADD CONSTRAINT FK_ASSET_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id)
GO

ALTER TABLE asset
    ADD CONSTRAINT FK_ASSET_ON_STOCKROOM FOREIGN KEY (stockroom_id) REFERENCES stockroom (id)
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'location'
EXEC sp_executesql @sql
GO

ALTER TABLE asset
    DROP COLUMN location
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'stockroom'
EXEC sp_executesql @sql
GO

ALTER TABLE asset
    DROP COLUMN stockroom
GO

INSERT INTO location (name)
VALUES ('EA-PL-GDN-GDNG'),
       ('EA-PL'),
       ('EA-PL-BIA-BIA'),
       ('EA-PL-eCS-BIB'),
       ('EA-PL-eCS-BZG'),
       ('EA-PL-eCS-GDN'),
       ('EA-PL-eCS-GLO'),
       ('EA-PL-eCS-GOW'),
       ('EA-PL-eCS-IEG'),
       ('EA-PL-eCS-KAL'),
       ('EA-PL-eCS-KLC'),
       ('EA-PL-eCS-KRK'),
       ('EA-PL-eCS-LOD'),
       ('EA-PL-eCS-LODT'),
       ('EA-PL-eCS-LUB'),
       ('EA-PL-eCS-OLS'),
       ('EA-PL-eCS-OPO'),
       ('EA-PL-eCS-OSZ'),
       ('EA-PL-eCS-PLO'),
       ('EA-PL-eCS-RDM'),
       ('EA-PL-eCS-SZZ'),
       ('EA-PL-eCS-SZZ-BRZOZWOWA.10'),
       ('EA-PL-eCS-TAR'),
       ('EA-PL-eCS-WAW'),
       ('EA-PL-eCS-ZAB'),
       ('EA-PL-FRE-POZW'),
       ('EA-PL-GDN'),
       ('EA-PL-GDN-GDN'),
       ('EA-PL-GDN-GDNGL'),
       ('EA-PL-GDN-OLS'),
       ('EA-PL-GDN-SLOWACZKIEGO.202B'),
       ('EA-PL-GDN-TRM'),
       ('EA-PL-JAW'),
       ('EA-PL-KRK-KRK'),
       ('EA-PL-KRK-KRKZ'),
       ('EA-PL-KTW'),
       ('EA-PL-KTW-GTU'),
       ('EA-PL-KTW-KRK'),
       ('EA-PL-KTW-RZE'),
       ('EA-PL-LOD'),
       ('EA-PL-LOD-TARGOWA.35'),
       ('EA-PL-OZR'),
       ('EA-PL-OZR-OZR'),
       ('EA-PL-OZR-OZREXP'),
       ('EA-PL-PEP-WAWD'),
       ('EA-PL-POZ'),
       ('EA-PL-POZ-GTA'),
       ('EA-PL-POZ-KON'),
       ('EA-PL-POZ-PASJONATOW.65'),
       ('EA-PL-POZ-PLA'),
       ('EA-PL-POZ-POZ'),
       ('EA-PL-POZ-ZLA'),
       ('EA-PL-QKD-QKDG'),
       ('EA-PL-SZZB'),
       ('EA-PL-SZZ-GTS'),
       ('EA-PL-SZZ-GWL'),
       ('EA-PL-SZZ-KSL'),
       ('EA-PL-TRN'),
       ('EA-PL-WAW'),
       ('EA-PL-WAW-GTW'),
       ('EA-PL-WAW-IAL'),
       ('EA-PL-WAW-KLC'),
       ('EA-PL-WAW-LDZ'),
       ('EA-PL-WAW-LUB'),
       ('EA-PL-WAW-PLO'),
       ('EA-PL-WAW-PRA'),
       ('EA-PL-WAW-RAD'),
       ('EA-PL-WAW-WAWP'),
       ('EA-PL-WAW-WAWW'),
       ('EA-PL-WAW-WIRAZOWA-37'),
       ('EA-PL-WAW-WLA'),
       ('EA-PL-WRO'),
       ('EA-PL-WRO-GTO'),
       ('EA-PL-WRO-RELAKSOWA.38'),
       ('EA-PL-WRO-WROS');

INSERT INTO stockroom (name)
VALUES ('EXP PL WAW'),
       ('EXP PL'),
       ('EXP PL POZ'),
       ('EXP PL OZR');
