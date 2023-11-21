package com.example.scheduling.exception;

/**
 * @Author: stayhungrystayfoollish
 * @Date: 2023/11/8
 * @Description:
 */
public class FileParseException extends Exception {
    public FileParseException(String message) {
        super(message);
    }

    public FileParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
