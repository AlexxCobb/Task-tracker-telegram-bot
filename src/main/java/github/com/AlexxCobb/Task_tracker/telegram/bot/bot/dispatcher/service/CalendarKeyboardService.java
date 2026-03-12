package github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.service;

import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.enums.CallbackType;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.mapper.CallbackDataMapper;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.callbackHandlers.model.CallbackDto;
import github.com.AlexxCobb.Task_tracker.telegram.bot.bot.dispatcher.util.DateTimeConstants;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class CalendarKeyboardService {

    public InlineKeyboardMarkup getCalendarKeyboard(Long taskId, YearMonth yearMonth, CallbackType source) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        rows.add(buildNavigationRow(taskId, yearMonth, source));
        rows.add(buildWeekDaysRow());
        rows.addAll(buildDaysRows(taskId, yearMonth, source));
        rows.add(buildCancelRow(taskId, source));

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    public InlineKeyboardMarkup getTimeKeyboard(Long taskId, LocalDate date, CallbackType source) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();
        var currentTime = LocalDate.now().equals(date) ? LocalTime.now() : null;

        for (int hour = 9; hour <= 23; hour++) {
            for (int minute : new int[]{0, 30}) {
                var time = LocalTime.of(hour, minute);

                if (currentTime != null && !time.isAfter(currentTime)) {
                    continue;
                }
                currentRow.add(buildTimeButton(taskId, date, hour, minute, source));

                if (currentRow.size() == 4) {
                    rows.add(new InlineKeyboardRow(currentRow));
                    currentRow = new ArrayList<>();
                }
            }
        }

        if (!currentRow.isEmpty()) {
            rows.add(new InlineKeyboardRow(currentRow));
        }

        if (rows.isEmpty()) {
            rows.add(new InlineKeyboardRow(List.of(
                    button("Нет доступного времени",
                           CallbackDto.builder().type(CallbackType.CALENDAR_IGNORE).build())
            )));
        }
        rows.add(buildManualTimeButton(taskId, date, source));
        rows.add(buildBackToCalendarRow(taskId, YearMonth.from(date), source));

        return InlineKeyboardMarkup.builder().keyboard(rows).build();
    }

    private InlineKeyboardRow buildNavigationRow(Long taskId, YearMonth yearMonth, CallbackType source) {
        var prevMonth = yearMonth.minusMonths(1);
        var nextMonth = yearMonth.plusMonths(1);

        var prevDto = CallbackDto.builder()
                .type(CallbackType.CALENDAR_NAVIGATE)
                .entityId(taskId)
                .source(source)
                .extra(prevMonth.toString())
                .build();

        var nextDto = CallbackDto.builder()
                .type(CallbackType.CALENDAR_NAVIGATE)
                .entityId(taskId)
                .source(source)
                .extra(nextMonth.toString())
                .build();

        var titleDto = CallbackDto.builder()
                .type(CallbackType.CALENDAR_IGNORE)
                .build();

        String monthTitle = yearMonth.getMonth()
                .getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("ru"))
                + " " + yearMonth.getYear();

        return new InlineKeyboardRow(List.of(
                button("◀", prevDto),
                button(monthTitle, titleDto),
                button("▶", nextDto)
        ));
    }

    private InlineKeyboardRow buildWeekDaysRow() {
        var ignore = CallbackDto.builder().type(CallbackType.CALENDAR_IGNORE).build();
        return new InlineKeyboardRow(
                Stream.of("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                        .map(day -> button(day, ignore))
                        .toList()
        );
    }

    private List<InlineKeyboardRow> buildDaysRows(Long taskId, YearMonth yearMonth, CallbackType source) {
        List<InlineKeyboardRow> rows = new ArrayList<>();

        var firstDay = yearMonth.atDay(1);
        var lastDay = yearMonth.atEndOfMonth();

        int firstDayOfWeek = firstDay.getDayOfWeek().getValue();

        List<InlineKeyboardButton> week = new ArrayList<>();

        var ignore = CallbackDto.builder().type(CallbackType.CALENDAR_IGNORE).build();
        for (int i = 1; i < firstDayOfWeek; i++) {
            week.add(button(" ", ignore));
        }

        for (int day = 1; day <= lastDay.getDayOfMonth(); day++) {
            var date = yearMonth.atDay(day);
            var dateStr = date.toString();

            var dto = CallbackDto.builder()
                    .type(CallbackType.CALENDAR_SELECT_DAY)
                    .entityId(taskId)
                    .source(source)
                    .extra(dateStr)
                    .build();

            boolean isPast = date.isBefore(LocalDate.now());
            if (isPast) {
                week.add(button("·", ignore));
            } else {
                week.add(button(String.valueOf(day), dto));
            }

            if (date.getDayOfWeek().getValue() == 7) {
                rows.add(new InlineKeyboardRow(week));
                week = new ArrayList<>();
            }
        }

        if (!week.isEmpty()) {
            rows.add(new InlineKeyboardRow(week));
        }

        return rows;
    }

    private InlineKeyboardButton button(String text, CallbackDto dto) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(CallbackDataMapper.toDataFromDto(dto))
                .build();
    }

    private InlineKeyboardButton buildTimeButton(Long taskId, LocalDate date, int hour, int minute,
                                                 CallbackType source) {
        var dateTime = date.atTime(LocalTime.of(hour, minute));
        var dateTimeStr = dateTime.format(DateTimeConstants.DATE_TIME_FORMATTER);

        var dto = CallbackDto.builder()
                .type(CallbackType.CALENDAR_SELECT_TIME)
                .entityId(taskId)
                .source(source)
                .extra(dateTimeStr)
                .build();

        String timeLabel = String.format("%d:%02d", hour, minute);
        return button(timeLabel, dto);
    }

    private InlineKeyboardRow buildManualTimeButton(Long taskId, LocalDate date, CallbackType source) {
        var dto = CallbackDto.builder()
                .type(CallbackType.MANUAL_SELECT_TIME)
                .entityId(taskId)
                .source(source)
                .extra(date.toString())
                .build();

        return new InlineKeyboardRow(List.of(button("Ввести время вручную", dto)));
    }

    private InlineKeyboardRow buildBackToCalendarRow(Long taskId, YearMonth yearMonth, CallbackType source) {
        var dto = CallbackDto.builder()
                .type(CallbackType.CALENDAR_NAVIGATE)
                .entityId(taskId)
                .source(source)
                .extra(yearMonth.toString())
                .build();

        return new InlineKeyboardRow(List.of(button("⬅ Назад к дате", dto)));
    }

    private InlineKeyboardRow buildCancelRow(Long taskId, CallbackType source) {
        var dto = resolveCancelDto(taskId, source);
        return new InlineKeyboardRow(List.of(button("❌ Отмена", dto)));
    }

    private CallbackDto resolveCancelDto(Long taskId, CallbackType source) {
        var type = source != null ? source : CallbackType.MAIN_MENU;
        return switch (type) {
            case SELECT_REMIND -> CallbackDto.builder()
                    .type(CallbackType.SHOW_REMINDERS)
                    .source(type)
                    .build();
            case MAIN_MENU -> CallbackDto.builder()
                    .type(CallbackType.MAIN_MENU)
                    .build();
            default -> CallbackDto.builder()
                    .type(CallbackType.SELECT_TASK)
                    .entityId(taskId)
                    .source(type)
                    .build();
        };
    }
}