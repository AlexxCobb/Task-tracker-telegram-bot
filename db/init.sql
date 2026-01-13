CREATE ROLE task_tracker_app WITH LOGIN PASSWORD 'abrakadabra';

CREATE SCHEMA telegram_bot AUTHORIZATION task_tracker_app;

GRANT ALL PRIVILEGES ON SCHEMA telegram_bot TO task_tracker_app;

ALTER ROLE task_tracker_app SET search_path TO telegram_bot;