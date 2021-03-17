package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Обрабатывает одну очередь сообщений для всех отправителей и получателей.
 */
public class QueueService implements Service {
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<String> emptyQueue;

    private ConcurrentLinkedQueue<String> getEmptyQueue() {
        if (emptyQueue == null) {
            emptyQueue = new ConcurrentLinkedQueue<>();
        }
        return emptyQueue;
    }

    private String addMessage(String queueName, String message) {
        queue.putIfAbsent(queueName, new ConcurrentLinkedQueue<>());
        queue.get(queueName).add(message);
        return message;
    }

    private String getMessage(String queueName) {
        String result = queue.getOrDefault(queueName, getEmptyQueue()).poll();
        return result == null ? "" : result;
    }

    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            return processRequest(() -> addHttpHeader(getMessage(req.queueName())));
        }
        if ("POST".equals(req.method())) {
            return processRequest(() -> addHttpHeader(addMessage(req.queueName(), req.text())));
        }
        return new Resp("", 404);
    }
}