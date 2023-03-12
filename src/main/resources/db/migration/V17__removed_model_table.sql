ALTER TABLE asset
    ADD model varchar(255)
GO

ALTER TABLE [order]
    ADD requested_model varchar(255)
GO

ALTER TABLE asset
    DROP CONSTRAINT FK_ASSET_ON_MODEL
GO

ALTER TABLE [order]
    DROP CONSTRAINT FK_ORDER_ON_REQUESTEDMODEL
GO

DROP TABLE asset_model
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'model_id'
EXEC sp_executesql @sql
GO

ALTER TABLE asset
    DROP COLUMN model_id
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE [order] DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'[order]')
  AND [c].[name] = N'requested_model_id'
EXEC sp_executesql @sql
GO

ALTER TABLE [order]
    DROP COLUMN requested_model_id
GO