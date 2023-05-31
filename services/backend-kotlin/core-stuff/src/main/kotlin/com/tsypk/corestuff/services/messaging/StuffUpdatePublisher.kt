package com.tsypk.corestuff.services.messaging

import com.fasterxml.jackson.databind.ObjectMapper
import com.tsypk.corestuff.controller.dto.stuff.request.BuyStuffRequest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Component

@Component
class StuffUpdatePublisher(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val stuffUpdateChannel: ChannelTopic,
    private val objectMapper: ObjectMapper,
) {
    fun publish(buyStuffRequest: BuyStuffRequest) {
        redisTemplate.convertAndSend(stuffUpdateChannel.topic, objectMapper.writeValueAsString(buyStuffRequest))
    }
}
