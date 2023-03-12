ALTER TABLE [order]
    ADD requester_id bigint
GO

ALTER TABLE [order]
    ADD CONSTRAINT FK_ORDER_ON_REQUESTER FOREIGN KEY (requester_id) REFERENCES [user] (id)
GO