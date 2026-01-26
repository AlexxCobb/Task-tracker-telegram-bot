CREATE TABLE telegram_user (
    chat_id BIGINT NOT NULL PRIMARY KEY,
    name VARCHAR(100)
);

COMMENT ON TABLE telegram_user IS 'Пользователи Telegram-бота';
COMMENT ON COLUMN telegram_user.chat_id IS 'ID чата в Telegram (первичный ключ)';
COMMENT ON COLUMN telegram_user.name IS 'Имя пользователя в Telegram';