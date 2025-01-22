package app.minkey.fr.minkeybackend.dto;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record UserResponse(Long id, String username, String email, String stripeCustId) {
}
