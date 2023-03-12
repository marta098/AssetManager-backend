ALTER TABLE global_variable
    ALTER COLUMN value varchar(255)
GO

INSERT INTO global_variable ([key], value)
VALUES ('crest_code', 'PL34523');