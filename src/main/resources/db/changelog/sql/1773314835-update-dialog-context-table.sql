ALTER TABLE telegram_bot.dialog_context
ADD COLUMN extra VARCHAR(50);

COMMENT ON COLUMN telegram_bot.dialog_context.extra
    IS 'Дополнительные данные диалога (например выбранная дата для напоминания)';