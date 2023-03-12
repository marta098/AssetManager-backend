ALTER TABLE [order]
    ADD is_dhl_reminder_sent bit
GO

ALTER TABLE [order]
    ADD is_it_reminder_sent bit
GO

UPDATE [order]
SET is_dhl_reminder_sent = 0;

UPDATE [order]
SET is_it_reminder_sent = 0;