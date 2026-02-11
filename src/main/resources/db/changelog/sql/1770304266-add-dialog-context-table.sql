CREATE TABLE dialog_context (
    user_chat_id BIGINT NOT NULL PRIMARY KEY,
    dialog_state VARCHAR(50) NOT NULL,
    task_id BIGINT,
    updated_at TIMESTAMPTZ
);

COMMENT ON TABLE dialog_context IS 'Контекст диалога пользователя (текущее состояние бота)';

COMMENT ON COLUMN dialog_context.user_chat_id IS 'ID чата Telegram (PK, 1 пользователь = 1 активный контекст)';
COMMENT ON COLUMN dialog_context.dialog_state IS 'Текущее состояние диалога (Enum DialogState)';
COMMENT ON COLUMN dialog_context.task_id IS 'ID задачи, с которой сейчас работает пользователь (если применимо)';
COMMENT ON COLUMN dialog_context.updated_at IS 'Время последнего изменения состояния диалога';

create index idx_dialog_context_user_chat_id on dialog_context (user_chat_id);