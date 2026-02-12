package github.com.AlexxCobb.Task_tracker.telegram.bot.bot;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.config.properties.BotProperties;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service.UpdateDispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Slf4j
public class TaskTrackerBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final BotProperties botProperties;
    private final UpdateDispatcher dispatcher;


    public TaskTrackerBot(BotProperties botProperties, UpdateDispatcher dispatcher) {
        this.botProperties = botProperties;
        this.dispatcher = dispatcher;
        this.telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return botProperties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        var messages = dispatcher.dispatch(update);
       messages.forEach(message ->{
           if(message instanceof SendMessage sendMessage){
               sendMessage(sendMessage);
           }
       });
    }


    private void sendMessage(SendMessage sendMessage) {
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error sending message: {}", e.getMessage());
        }
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        System.out.println("Registered bot running state is: " + botSession.isRunning());
    }
}
