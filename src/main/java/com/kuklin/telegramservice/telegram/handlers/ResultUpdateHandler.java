package com.kuklin.telegramservice.telegram.handlers;

import com.kuklin.telegramservice.entities.TelegramUser;
import com.kuklin.telegramservice.integrations.AiConversationFeignClient;
import com.kuklin.telegramservice.integrations.InterviewFeignClient;
import com.kuklin.telegramservice.services.TelegramService;
import com.kuklin.telegramservice.sharedlibrary.ConversationDto;
import com.kuklin.telegramservice.sharedlibrary.InterviewDto;
import com.kuklin.telegramservice.sharedlibrary.MessageRequestDto;
import com.kuklin.telegramservice.sharedlibrary.MessageResponseDto;
import com.kuklin.telegramservice.telegram.utils.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ResultUpdateHandler implements UpdateHandler {
    private final InterviewFeignClient interviewFC;
    private final TelegramService telegramService;
    private final AiConversationFeignClient aiConversationFC;
    private static final String EMPTY_ERROR = "Сначала закончите хотя бы одно собеседование";
    private static final String AI_REQUEST = """
            Сейчас тебе будут отправлены результаты моих нескольких собеседований.
            Тебе нужно сделать общий анализ. Проанализировать мои результаты, 
            сказать что я достиг, что еще предстоит улучшить.\n 
            """;
    @Override
    public void handle(Update update, TelegramUser telegramUser) {
        Message requestMessage = update.getMessage();
        Long chatId = requestMessage.getChatId();

        List<InterviewDto> interviewList = interviewFC.getLatestResultList(telegramUser.getUserId());
        if (interviewList.isEmpty()) {
            telegramService.sendReturnedMessage(chatId,EMPTY_ERROR);
            return;
        }

        ConversationDto conversation = aiConversationFC.postNewConversationDto(
                new ConversationDto()
                        .setUserId(telegramUser.getUserId()));

        String results = AI_REQUEST + interviewList.stream()
                .map(InterviewDto::getResult)
                .collect(Collectors.joining("\n"));

        MessageRequestDto messageRequestDto = MessageRequestDto.getDefault(results, conversation.getId());
        messageRequestDto.setUserId(telegramUser.getUserId());
        MessageResponseDto responseDto = aiConversationFC.sendUserMessage(messageRequestDto);

        telegramService.sendReturnedMessage(chatId, responseDto.getContent());


    }

    @Override
    public String getHandlerListName() {
        return Command.RESULTS.getCommandText();
    }
}
