package com.authserve.authproduct.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "username", "message", "status", "jwt" })
public record AuthResponse(
        String message,
        String jwt,
        boolean status) {

}
