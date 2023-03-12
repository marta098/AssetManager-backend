ALTER TABLE [user]
DROP
COLUMN password
GO

ALTER TABLE [user]
    ADD password nvarchar(max)
    GO