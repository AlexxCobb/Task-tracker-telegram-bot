CREATE TABLE reminder (
    reminder_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    task_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    remind_at TIMESTAMPTZ NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    sent_at TIMESTAMPTZ,

    CONSTRAINT fk_reminder_task FOREIGN KEY (task_id)
        REFERENCES task(task_id) ON DELETE CASCADE
);

COMMENT ON TABLE reminder IS 'Напоминания по задачам';

COMMENT ON COLUMN reminder.reminder_id IS 'ID напоминания';
COMMENT ON COLUMN reminder.task_id IS 'Ссылка на задачу';
COMMENT ON COLUMN reminder.chat_id IS 'Telegram chat_id пользователя';
COMMENT ON COLUMN reminder.remind_at IS 'Время отправки напоминания (UTC)';
COMMENT ON COLUMN reminder.status IS 'Статус: SCHEDULED, SENT, CANCELLED';
COMMENT ON COLUMN reminder.created_at IS 'Дата создания напоминания';
COMMENT ON COLUMN reminder.sent_at IS 'Фактическое время отправки';

CREATE INDEX idx_reminder_task_id ON reminder (task_id);
CREATE INDEX idx_reminder_status ON reminder (status);
CREATE INDEX idx_reminder_due_lookup ON reminder (status, remind_at);