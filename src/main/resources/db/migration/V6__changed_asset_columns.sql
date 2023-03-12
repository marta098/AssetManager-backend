ALTER TABLE asset
    ADD deprecation datetime
    GO

ALTER TABLE asset
    ADD mpk_number varchar(255)
    GO

DECLARE
@sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
    INNER JOIN [sys].[default_constraints] AS [df]
ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'crest_number'
    EXEC sp_executesql @sql
    GO

ALTER TABLE asset
DROP
COLUMN crest_number
GO

DECLARE
@sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
    INNER JOIN [sys].[default_constraints] AS [df]
ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset')
  AND [c].[name] = N'warranty_expiration'
    EXEC sp_executesql @sql
    GO

ALTER TABLE asset
DROP
COLUMN warranty_expiration
GO