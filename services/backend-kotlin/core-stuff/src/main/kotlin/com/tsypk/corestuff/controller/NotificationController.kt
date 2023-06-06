package com.tsypk.corestuff.controller

import com.tsypk.corestuff.controller.dto.notification.NotificationActionType
import com.tsypk.corestuff.controller.dto.notification.NotificationDto
import com.tsypk.corestuff.controller.dto.notification.NotificationStuffRequest
import com.tsypk.corestuff.controller.dto.notification.NotificationSubscriptionDto
import com.tsypk.corestuff.model.notification.NotificationSubscription
import com.tsypk.corestuff.model.notification.NotificationType
import com.tsypk.corestuff.model.notification.toDto
import com.tsypk.corestuff.repository.NotificationRepository
import com.tsypk.corestuff.repository.NotificationSubscriptionRepository
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/notification")
class NotificationController(
    private val subscriptionRepository: NotificationSubscriptionRepository,
    private val notificationRepository: NotificationRepository,
) {
    @GetMapping(value = ["/list"])
    @Transactional
    fun getSupplierStuffFromId(@RequestHeader("UserID") userId: Long): ResponseEntity<List<NotificationDto>> {
        val found = notificationRepository.findAllForUser(userId)
        return ResponseEntity.ok(found.map { it.toDto() })
    }

    @GetMapping(value = ["/subscription/list"])
    fun getNotificationSubscriptions(
        @RequestHeader("UserID") userId: Long,
    ): ResponseEntity<List<NotificationSubscriptionDto>> {
        val found = subscriptionRepository.getAllForUser(userId)
        return ResponseEntity.ok(found.map { it.toDto() })
    }

    @PostMapping(
        value = ["/stuff"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun addStuffForSupplier(
        @RequestHeader("UserID") userId: Long,
        @RequestBody request: NotificationStuffRequest
    ): ResponseEntity<Unit> {
        if (request.actionType == NotificationActionType.SUBSCRIBE) {
            subscriptionRepository.create(
                notificationSubscription = NotificationSubscription(
                    userId = userId,
                    entityId = request.modelId,
                    type = NotificationType.STUFF_SUBSCRIPTION,
                )
            )
        } else {
            subscriptionRepository.delete(
                userId = userId,
                entityId = request.modelId,
                type = NotificationType.STUFF_SUBSCRIPTION,
            )
        }

        return ResponseEntity.ok(Unit)
    }
}