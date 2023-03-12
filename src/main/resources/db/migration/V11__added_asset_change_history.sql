CREATE TABLE asset_change_history
(
    id              bigint IDENTITY (1, 1) NOT NULL,
    asset_id        bigint,
    owner_id        bigint,
    author_id       bigint,
    timestamp       datetime,
    assignment_date datetime,
    discharge_date  datetime,
    deprication     datetime,
    remark          nvarchar(max),
    status          int,
    CONSTRAINT pk_assetchangehistory PRIMARY KEY (id)
)
GO

ALTER TABLE asset_change_history
    ADD CONSTRAINT FK_ASSETCHANGEHISTORY_ON_ASSET FOREIGN KEY (asset_id) REFERENCES asset (id)
GO

ALTER TABLE asset_change_history
    ADD CONSTRAINT FK_ASSETCHANGEHISTORY_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES [user] (id)
GO

ALTER TABLE asset_change_history
    ADD CONSTRAINT FK_ASSETCHANGEHISTORY_ON_OWNER FOREIGN KEY (owner_id) REFERENCES [user] (id)
GO