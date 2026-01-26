create TABLE task (
    task_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_chat_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    status VARCHAR(20) NOT NULL,
    type VARCHAR(20) NOT NULL ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    is_shopping_list BOOLEAN DEFAULT FALSE,

    CONSTRAINT fk_task_user FOREIGN KEY (user_chat_id) REFERENCES telegram_user(chat_id) ON delete CASCADE
);

comment on table task is 'Задачи пользователей';
comment on column task.task_id is 'ID задачи';
comment on column task.user_chat_id is 'Ссылка на пользователя Telegram';
comment on column task.title is 'Название задачи';
comment on column task.status is 'Статус задачи: NEW, DONE';
comment on column task.type is 'Тип задачи: TASK, EPIC_TASK';
comment on column task.created_at is 'Дата создания задачи';
comment on column task.updated_at is 'Дата последнего обновления';
comment on column task.is_shopping_list is 'Флаг, определящий является ли задача списком покупок';

create index idx_task_user_chat_id on task (user_chat_id);
create index idx_task_status on task (status);
create index idx_task_type on task (type);
create index idx_task_created_at on task (created_at desc);