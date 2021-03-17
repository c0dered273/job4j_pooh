package ru.job4j.pooh;

import java.util.Objects;

/**
 * Используется как ключ для словаря очередей в TopicService
 * для обращения к очередям не только по имени, но и по Id потребителя.
 */
public class TopicKey {
    private final String queueName;
    private final String consumerId;

    public TopicKey(String queueName, String consumerId) {
        this.queueName = queueName;
        this.consumerId = consumerId;
    }

    public String getQueueName() {
        return queueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TopicKey topicKey = (TopicKey) o;
        return Objects.equals(queueName, topicKey.queueName)
                && Objects.equals(consumerId, topicKey.consumerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queueName, consumerId);
    }
}
