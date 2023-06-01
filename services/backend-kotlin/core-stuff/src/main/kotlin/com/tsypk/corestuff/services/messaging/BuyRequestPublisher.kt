package com.tsypk.corestuff.services.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequestEvent
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class BuyRequestPublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val buyRequestsChannel: ChannelTopic,
    private val objectMapper: ObjectMapper,
) {
    fun publish(buyStuffRequestEvent: BuyStuffRequestEvent) {
        redisTemplate.convertAndSend(buyRequestsChannel.topic, objectMapper.writeValueAsString(buyStuffRequestEvent))
    }

    @Scheduled(fixedDelay = 5000L)
    fun publishCron() {
        publish(
            BuyStuffRequestEvent(
                buyerId = 1L,
                supplierId = 0L,
                modelId = "IPHONE_14_PRO_MAX/GB_512/SPACE_BLACK",
                count = 3,
            )
        )
    }
}
