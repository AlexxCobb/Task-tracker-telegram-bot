package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.commandHandlers;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.UpdateContext;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.KeyboardService;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StartCommandHandler implements UpdateHandler {

    private final KeyboardService keyboardService;

    @Override
    public boolean canHandle(UpdateContext context) {
        return context.isTextMessage() && context.getText().equals("/start");
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(UpdateContext context) {
        var chatId = context.chatId();
        var user = context.update().getMessage().getFrom();
        var firstName = user.getFirstName() != null ? user.getFirstName() : "друг";

        String text = context.isNewUser()
                ? """
              *Привет, %s\\!* 👋
              
              Добро пожаловать в *Task Tracker* 📋
              
              Я помогу тебе:
              • создавать задачи  
              • добавлять подзадачи  
              • вести список покупок 🛒  
              • отмечать выполненное ✅  
              
              Выбери действие ниже 👇
              """.formatted(firstName)
                : """
              *С возвращением, %s\\!* 👋
              
              Рад снова видеть тебя 😎  
              
              Продолжим управлять задачами?
              """.formatted(firstName);

        return List.of(
                SendMessage.builder()
                        .chatId(chatId)
                        .text(text)
                        .parseMode("MarkdownV2")
                        .replyMarkup(keyboardService.getStartKeyboard())
                        .build()
        );
    }
}
