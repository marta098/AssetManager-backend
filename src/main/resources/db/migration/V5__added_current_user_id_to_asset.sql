ALTER TABLE asset
    ADD current_user_id bigint
GO

ALTER TABLE asset
    ADD CONSTRAINT FK_ASSET_ON_CURRENTUSER FOREIGN KEY (current_user_id) REFERENCES [user] (id)
GO