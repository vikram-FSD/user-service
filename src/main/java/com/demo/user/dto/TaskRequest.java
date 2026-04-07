package com.demo.user.dto;

import java.time.LocalDateTime;

public record TaskRequest(String id, String userId, String name, String description, String status, LocalDateTime updated_at) {

}
