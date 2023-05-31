package com.tsypk.corestuff.services.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.tsypk.corestuff.controller.dto.stuff.StuffType
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
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
    fun publish(buyStuffRequest: BuyStuffRequest) {
        redisTemplate.convertAndSend(buyRequestsChannel.topic, objectMapper.writeValueAsString(buyStuffRequest))
    }

    @Scheduled(fixedDelay = 5000L)
    fun publishCron() {
        publish(
            BuyStuffRequest(
                supplierId = 0L,
                stuffType = StuffType.IPHONE,
                modelId = "IPHONE_14_PRO_MAX/GB_512/SPACE_BLACK",
                count = 3,
            )
        )
    }
}
