package app.minkey.fr.minkeybackend.auth;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record ForgotReq(@Email String email) {
}
