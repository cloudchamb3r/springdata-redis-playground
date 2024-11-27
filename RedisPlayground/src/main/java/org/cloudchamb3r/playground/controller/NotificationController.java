package org.cloudchamb3r.playground.controller;

import lombok.RequiredArgsConstructor;
import org.cloudchamb3r.playground.domain.NotificationPublishRequest;
import org.cloudchamb3r.playground.service.NotificationService;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/publish")
    public void publish(@RequestBody NotificationPublishRequest publishRequest) {
        notificationService.publish(publishRequest.message());
    }

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
       return notificationService.subscribe();
    }
}
