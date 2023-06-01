package com.tsypk.corestuff.services.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.tsypk.corestuff.model.notification.PriceUpdate
import com.tsypk.corestuff.model.notification.StuffUpdateBatchEvent
import com.tsypk.corestuff.model.notification.StuffUpdateEvent
import com.tsypk.corestuff.model.notification.StuffUpdateType
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class StuffUpdatePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val stuffUpdateChannel: ChannelTopic,
    private val objectMapper: ObjectMapper,
) {
    fun publish(stuffUpdateBatchEvent: StuffUpdateBatchEvent) {
        redisTemplate.convertAndSend(stuffUpdateChannel.topic, objectMapper.writeValueAsString(stuffUpdateBatchEvent))
    }

    @Scheduled(fixedDelay = 5000L)
    fun publishCron() {
        publish(
            StuffUpdateBatchEvent(
                listOf(
                    StuffUpdateEvent(
                        type = StuffUpdateType.UPDATE,
                        modelId = "IPHONE_14_PRO_MAX/GB_512/SPACE_BLACK",
                        payload = PriceUpdate(
                            oldPrice = 1000L,
                            newPrice = 999,
                        )
                    ),
                    StuffUpdateEvent(
                        type = StuffUpdateType.CREATE,
                        modelId = "IPHONE_14_PRO_MAX/GB_512/WHITE",
                    ),
                    StuffUpdateEvent(
                        type = StuffUpdateType.DELETE,
                        modelId = "IPHONE_13/GB_512/BLUE",
                    )
                )
            )
        )
    }
}
