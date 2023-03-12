ALTER TABLE [order]
    ADD delivery_address nvarchar (max)
    GO

ALTER TABLE [order]
    ADD order_number nvarchar (max)
    GO

ALTER TABLE [order]
    ADD receiver_id bigint
    GO

ALTER TABLE [order]
    ADD added_date datetime2
    GO

ALTER TABLE [order]
    ADD CONSTRAINT FK_ORDER_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES [user] (id)
    GO