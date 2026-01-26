CREATE TABLE subtask (
    subtask_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    task_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_subtask_task FOREIGN KEY (task_id)
        REFERENCES task(task_id) ON DELETE CASCADE
);

COMMENT ON TABLE subtask IS 'Подзадачи для EPIC_TASK';
COMMENT ON COLUMN subtask.subtask_id IS 'ID подзадачи';
COMMENT ON COLUMN subtask.task_id IS 'Ссылка на родительскую задачу';
COMMENT ON COLUMN subtask.title IS 'Название подзадачи';
COMMENT ON COLUMN subtask.status IS 'Статус подзадачи: NEW, DONE';
COMMENT ON COLUMN subtask.created_at IS 'Дата создания подзадачи';
COMMENT ON COLUMN subtask.updated_at IS 'Дата последнего обновления';

CREATE INDEX idx_subtask_task_id ON subtask (task_id);
CREATE INDEX idx_subtask_status ON subtask (status);