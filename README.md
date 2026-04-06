![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

# 📋 Task Tracker Telegram Bot

Телеграм-бот для управления задачами с поддержкой подзадач, списков покупок и напоминаний.

**Бот:** [@toDo_taskTracker_bot](https://t.me/toDo_taskTracker_bot)

---

## Возможности

### Задачи
- ✅ Создание задач и отметка выполнения
- 🧩 Задачи с подзадачами (epic-задачи)
- ✏️ Редактирование названия задачи
- 🗑️ Удаление задач и массовое удаление выполненных
- 📋 Фильтрация: все / активные / завершённые

### Списки покупок
- 🛒 Создание списка покупок с отдельными позициями
- ☑️ Отметка каждой позиции
- 🗑️ Удаление списков

### Напоминания
- ⏰ Установка напоминания для любой задачи
- 📅 Выбор даты через интерактивный календарь прямо в боте
- 🕐 Выбор времени слотами по 30 минут (09:00 — 23:30)
- ✍️ Ввод любого времени вручную в формате `ЧЧ:ММ` или `Ч:ММ` (например `14:30` или `9:00`)
- 🔔 Автоматическая отправка уведомления в назначенное время
- 📋 Просмотр всех активных напоминаний через `/reminders`
- 🗑️ Удаление напоминания

---

## Команды

| Команда | Описание |
|---------|----------|
| `/start` | Запустить бота / вернуться в главное меню |
| `/reminders` | Показать все активные напоминания |
 
---

## Стек технологий

| Слой | Технология |
|------|-----------|
| Язык | Java 21 |
| Фреймворк | Spring Boot 3.5 |
| БД | PostgreSQL 16 |
| Миграции | Liquibase |
| ORM | Spring Data JPA / Hibernate |
| Telegram API | TelegramBots 9.2 (long polling) |
| Планировщик | Spring Scheduler (`@Scheduled`) |
| Инфраструктура | Docker Compose |
 
---

# Архитектура

Бот построен на паттерне **Chain of Responsibility**:

```
Telegram Update
      ↓
UpdateDispatcher
      ↓
List<UpdateHandler> — перебирает хендлеры через canHandle()
      ↓
первый подходящий handler.handle()
      ↓
List<BotApiMethod> — ответ в Telegram
```

### Флоу установки напоминания

```
Нажать "Добавить напоминание"
      ↓
Выбор даты в календаре
      ↓
      ├── Выбрать слот времени (30 мин интервалы)
      │         или
      └── Ввести время вручную (ЧЧ:ММ / Ч:ММ)
                ↓
      Напоминание сохранено
```

### Обработка напоминаний планировщиком

```
ReminderScheduler (@Scheduled каждые 60 сек)
      ↓
ReminderUseCaseService.sendReminderUseCase()
      ↓
ReminderService.lockDetails()          — SELECT FOR UPDATE SKIP LOCKED
      ↓
ReminderNotificationService.sendAll()  — отправка через TelegramClient
      ↓
ReminderService.markSent()             — UPDATE status = SENT
```

`SELECT FOR UPDATE SKIP LOCKED` обеспечивает корректную обработку при масштабировании до нескольких инстансов.
- Часовой пояс напоминаний фиксирован — UTC+3 (Москва)
---


## 📄 Лицензия

Проект распространяется под лицензией MIT. Подробнее в файле [LICENSE](LICENSE).