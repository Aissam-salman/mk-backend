package app.minkey.fr.minkeybackend.dto;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record UserResponse (String username, String email, Timestamp subscribeAt) {
}
