package app.minkey.fr.minkeybackend.dto;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record ForgotReq(@Email String email) {
}
