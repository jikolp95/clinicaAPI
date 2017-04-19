package org.jikolp.model;

/**
 * Описание шаблона ответа
 */

public class ServerAnswer {
    //описание ошибки
    private String message;
    //код ошибки
    private int code;

    public ServerAnswer() {
    }

    public ServerAnswer(String message, int code) {
        super();
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
