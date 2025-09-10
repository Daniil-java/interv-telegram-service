package com.kuklin.telegramservice.telegram.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum BotState {
    WAIT,
    INTERVIEW,
    TOPIC_INTERVIEW;


}
