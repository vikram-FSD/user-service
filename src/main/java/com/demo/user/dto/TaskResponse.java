package com.demo.user.dto;

import java.util.UUID;

public record TaskResponse(UUID id, String name, String description, String status) {

}
