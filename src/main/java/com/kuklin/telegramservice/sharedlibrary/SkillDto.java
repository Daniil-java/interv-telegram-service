package com.kuklin.telegramservice.sharedlibrary;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class SkillDto {
    private Long id;
    private String name;
    private String category;
    private Set<TopicDto> topics;
}
