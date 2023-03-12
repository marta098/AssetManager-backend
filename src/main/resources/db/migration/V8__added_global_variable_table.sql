CREATE TABLE global_variable
(
    [key] nvarchar
(
    255
) NOT NULL,
    value int,
    CONSTRAINT pk_globalvariable PRIMARY KEY ([key])
    ) GO

INSERT INTO global_variable ([key], value)
VALUES ('deprecation_months', 48);