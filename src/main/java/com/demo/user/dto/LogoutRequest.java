package com.demo.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LogoutRequest(
        @NotEmpty(message = "Access token is required") @NotBlank(message = "Access token is required") String accessToken) {

}
