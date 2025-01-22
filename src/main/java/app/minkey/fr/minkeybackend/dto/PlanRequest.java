package app.minkey.fr.minkeybackend.dto;

import lombok.Builder;

@Builder
public record PlanRequest(String email, String stripeCustId, String plan) {
}
