package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.config;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.config.properties.BotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TelegramConfig {

    private final BotProperties botProperties;

    @Bean
    public TelegramClient telegramClient() {
        return new OkHttpTelegramClient(botProperties.token());
    }
}
