DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE asset_change_history DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'asset_change_history')
  AND [c].[name] = N'status'
EXEC sp_executesql @sql
GO

ALTER TABLE asset_change_history
    DROP COLUMN status
GO

ALTER TABLE asset_change_history
    ADD status nvarchar(255)
GO