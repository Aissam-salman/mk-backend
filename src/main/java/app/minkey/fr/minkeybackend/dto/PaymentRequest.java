package app.minkey.fr.minkeybackend.dto;

import lombok.Builder;

@Builder
public record PaymentRequest(Long amount) {
}
