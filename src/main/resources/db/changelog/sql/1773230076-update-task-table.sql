UPDATE telegram_bot.task
SET type = 'SHOPPING_LIST'
WHERE is_shopping_list = true;

ALTER TABLE telegram_bot.task
DROP COLUMN is_shopping_list;