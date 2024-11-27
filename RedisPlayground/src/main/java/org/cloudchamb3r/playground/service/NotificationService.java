package org.cloudchamb3r.playground.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class NotificationService implements MessageListener {
    private final String NOTIFICATION_QUEUE_NAME = "notification";
    private final StringRedisTemplate redisTemplate;
    private CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();


    public void publish(String message) {
        redisTemplate.convertAndSend(NOTIFICATION_QUEUE_NAME, message);
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitters.add(emitter);
        return emitter;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody());
        emitters.forEach((emitter) -> {
            try {
                SseEventBuilder sseEventBuilder = SseEmitter.event()
                                .name("notification")
                                .data(messageBody);
                emitter.send(sseEventBuilder);
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
    }
}
