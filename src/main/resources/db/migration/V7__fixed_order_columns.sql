CREATE TABLE asset_model
(
    id    bigint IDENTITY (1, 1) NOT NULL,
    model nvarchar(max),
    CONSTRAINT pk_assetmodel PRIMARY KEY (id)
)
GO

CREATE TABLE mpk_number
(
    id  bigint IDENTITY (1, 1) NOT NULL,
    mpk nvarchar(max),
    CONSTRAINT pk_mpknumber PRIMARY KEY (id)
)
GO

ALTER TABLE asset
    ADD model_id bigint
GO

ALTER TABLE [order]
    ADD mpk_number_id bigint
GO

ALTER TABLE [order]
    ADD requested_model_id bigint
GO

ALTER TABLE asset
    ADD CONSTRAINT FK_ASSET_ON_MODEL FOREIGN KEY (model_id) REFERENCES asset_model (id)
GO

ALTER TABLE [order]
    ADD CONSTRAINT FK_ORDER_ON_MPKNUMBER FOREIGN KEY (mpk_number_id) REFERENCES mpk_number (id)
GO

ALTER TABLE [order]
    ADD CONSTRAINT FK_ORDER_ON_REQUESTEDMODEL FOREIGN KEY (requested_model_id) REFERENCES asset_model (id)
GO

DECLARE
    @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'model'
EXEC sp_executesql @sql
GO

ALTER TABLE asset
    DROP
        COLUMN model
GO

DECLARE
    @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'mpk_number'
EXEC sp_executesql @sql
GO

ALTER TABLE asset
    DROP
        COLUMN mpk_number
GO

INSERT INTO asset_model (model)
VALUES ('HP EliteBook G1'),
       ('HP EliteBook G2'),
       ('HP EliteBook G3'),
       ('HP EliteBook G4'),
       ('HP EliteBook G5'),
       ('HP EliteBook G6'),
       ('HP EliteBook G7'),
       ('HP EliteBook G8'),
       ('HP EliteBook G9'),
       ('HP ProDesk G1'),
       ('HP ProDesk G2'),
       ('HP ProDesk G3'),
       ('HP ProDesk G4'),
       ('HP ProDesk G5'),
       ('HP ProDesk G6'),
       ('HP ProDesk G7'),
       ('HP ProDesk G8'),
       ('HP ProDesk G9'),
       ('HP Elite x2 1012 G1');

INSERT INTO mpk_number (mpk)
VALUES ('MPK620600'),
       ('MPK620700'),
       ('MPK690800');