package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Одна входящая очередь для сообщений, при обращении потребителя, для него создается
 * индивидуальная очередь, в которую копируются сообщения из общей очереди.
 * Сообщения, поступающие в общую очередь, копируются в индивидуальные очереди потребителей.
 */
public class TopicService implements Service {
    private static final ConcurrentLinkedQueue<String> emptyQueue = new ConcurrentLinkedQueue<>();
    private final ConcurrentHashMap<TopicKey, ConcurrentLinkedQueue<String>> queue =
            new ConcurrentHashMap<>();

    private String addMessage(String queueName, String message) {
        queue.putIfAbsent(new TopicKey(queueName, ""), new ConcurrentLinkedQueue<>());
        queue.forEach((tk, q) -> {
            if (queueName.equals(tk.getQueueName())) {
                q.add(message);
            }
        });
        return message;
    }

    private String getMessage(String queueName, String clientId) {
        TopicKey mainQueueKey = new TopicKey(queueName, "");
        TopicKey topicKey = new TopicKey(queueName, clientId);
        ConcurrentLinkedQueue<String> mainQueue = queue.getOrDefault(mainQueueKey, emptyQueue);
        if (mainQueue.isEmpty()) {
            return "";
        }
        queue.putIfAbsent(topicKey, new ConcurrentLinkedQueue<>(mainQueue));
        String result = queue.getOrDefault(topicKey, emptyQueue).poll();
        return result == null ? "" : result;
    }

    @Override
    public Resp process(Req req) {
        if ("GET".equals(req.method())) {
            return processRequest(() -> addHttpHeader(getMessage(req.queueName(), req.clientId())));
        }
        if ("POST".equals(req.method())) {
            return processRequest(() -> addHttpHeader(addMessage(req.queueName(), req.text())));
        }
        return new Resp("", 404);
    }
}