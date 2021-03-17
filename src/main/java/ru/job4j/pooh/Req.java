package ru.job4j.pooh;

/**
 * Парсит входящее сообщение, хранит параметры запроса и полезную нагрузку из сообщения.
 */
public class Req {
    private final String method;
    private final String mode;
    private final String queueName;
    private final String clientId;
    private final String text;

    private Req(String method, String mode, String queueName, String clientId, String text) {
        this.method = method;
        this.mode = mode;
        this.queueName = queueName;
        this.clientId = clientId;
        this.text = text;
    }

    /**
     * Возвращает распарсенное сообщение.
     *
     * <p>POST /topic/weather HTTP/1.1</p>
     * Host: localhost:9000
     * User-Agent: curl/7.68.0
     * Accept:
     * Content-Length: 14
     * Content-Type: application/x-www-form-urlencoded
     *
     * <p>temperature=21</p>
     *
     * @param content Входящее сообщение
     * @return Req с результатом
     */
    public static Req of(String content) {
        var strings = content.split("\\r?\\n");
        var fstStr = strings[0].split("\\s+");
        var method = fstStr[0];
        var modeAndQueue = fstStr[1].split("/");
        var mode = modeAndQueue[1];
        var queueName = modeAndQueue[2];
        String clientId = "";
        if (modeAndQueue.length >= 4) {
            clientId = modeAndQueue[3];
        }
        var text = "";
        if ("POST".equals(method)) {
            text = strings[strings.length - 1];
        }
        return new Req(method, mode, queueName, clientId, text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String queueName() {
        return queueName;
    }

    public String clientId() {
        return clientId;
    }

    public String text() {
        return text;
    }
}