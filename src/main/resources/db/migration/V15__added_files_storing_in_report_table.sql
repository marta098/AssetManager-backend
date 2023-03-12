ALTER TABLE report
    ADD [file] varbinary(MAX)
GO

DECLARE @sql [nvarchar](MAX)
SELECT @sql = N'ALTER TABLE report DROP CONSTRAINT ' + QUOTENAME([df].[name])
FROM [sys].[columns] AS [c]
         INNER JOIN [sys].[default_constraints] AS [df] ON [df].[object_id] = [c].[default_object_id]
WHERE [c].[object_id] = OBJECT_ID(N'report')
  AND [c].[name] = N'link'
EXEC sp_executesql @sql
GO

ALTER TABLE report
    DROP COLUMN link
GO