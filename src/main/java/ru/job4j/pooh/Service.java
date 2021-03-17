package ru.job4j.pooh;

import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * Обрабатывает запросы для пользователя.
 */
public interface Service {
    Resp process(Req req);

    /**
     * Добавляет статус в зависимости от результата выполнения запроса.
     *
     * @param supplier Функциональный интерфейс, возвращает результат запроса
     * @return Resp ответ
     */
    default Resp processRequest(Supplier<String> supplier) {
        int status = 501;
        String message = supplier.get();
        if (!message.isEmpty()) {
            status = 200;
        }
        return new Resp(message, status);
    }

    /**
     * Добавляет в ответу минимальный HTTP заголовок.
     *
     * @param text Полезная нагрузка
     * @return Готовое для отправки сообщение
     */
    default String addHttpHeader(String text) {
        StringJoiner responseText = new StringJoiner("\r\n");
        responseText.add("Content-Length: " + text.length());
        responseText.add("Content-Type: text/plain; charset=utf-8");
        responseText.add("");
        responseText.add(text);
        return responseText.toString();
    }
}
