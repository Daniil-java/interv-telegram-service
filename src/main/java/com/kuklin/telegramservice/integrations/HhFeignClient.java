package com.kuklin.telegramservice.integrations;

import com.kuklin.telegramservice.sharedlibrary.HhResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        value = "hh-feign-client",
        url = "${integrations.hh-api.url}"
)
public interface HhFeignClient {

    @GetMapping("/vacancies/{vacancyId}")
    HhResponseDto getVacancyById(@PathVariable Long vacancyId);
}
