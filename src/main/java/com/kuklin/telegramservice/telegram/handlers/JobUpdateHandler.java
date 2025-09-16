package com.kuklin.telegramservice.telegram.handlers;

import com.kuklin.telegramservice.entities.TelegramUser;
import com.kuklin.telegramservice.integrations.InterviewFeignClient;
import com.kuklin.telegramservice.services.TelegramService;
import com.kuklin.telegramservice.telegram.utils.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class JobUpdateHandler implements UpdateHandler {

    private final TelegramService telegramService;
    private final InterviewFeignClient interviewFC;
    private static final String JOB_ERROR_MESSAGE =
            "Произошла ошибка! Введите существующую должность, либо вернитесь позжею";
    private static final String SUCCESS_MESSAGE = "Новая должность сохранена: ";

    @Override
    public void handle(Update update, TelegramUser telegramUser) {
        Message requestMessage = update.getMessage();
        Long chatId = requestMessage.getChatId();

        //Извлечение названия вакансии из сообщения
        String jobTitle = requestMessage.getText().substring(
                getHandlerListName().length());

        String response = JOB_ERROR_MESSAGE;
        //true - если получилось изменить вакансию
        if (interviewFC.setJobTitle(telegramUser.getUserId(), jobTitle)) {
            response = SUCCESS_MESSAGE + jobTitle;
        }
        telegramService.sendReturnedMessage(chatId, response);

    }

    @Override
    public String getHandlerListName() {
        return Command.JOB.getCommandText();
    }
}
