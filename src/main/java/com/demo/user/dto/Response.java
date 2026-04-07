package com.demo.user.dto;

import java.time.LocalDateTime;

public class Response {

    public record ResponseData(LocalDateTime timestamp, int status, String message, Object data) {
    }

    public record ResponseMessage(LocalDateTime timestamp, int status, String message) {
    }
}
