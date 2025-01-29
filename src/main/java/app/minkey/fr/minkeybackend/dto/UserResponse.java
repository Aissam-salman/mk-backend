package app.minkey.fr.minkeybackend.dto;

import app.minkey.fr.minkeybackend.user.model.Plan;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record UserResponse(Long id, String username, String email, String stripeCustId, String photoUrl, Plan plan) {
}
