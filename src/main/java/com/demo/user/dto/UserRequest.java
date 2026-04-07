package com.demo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotEmpty(message = "Name cannot be empty") @NotNull @NotBlank(message = "Name is required") String name,

        @NotNull @NotBlank(message = "Email is required") @Email String email,

        @Size(min = 5, message = "Password must contains atleast 5 characters") @NotNull @NotBlank(message = "Password is required") String password)

{
}
