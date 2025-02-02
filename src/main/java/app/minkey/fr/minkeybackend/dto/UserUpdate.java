package app.minkey.fr.minkeybackend.dto;

import app.minkey.fr.minkeybackend.user.model.Plan;
import jakarta.validation.constraints.Email;
import lombok.Builder;


@Builder
public record UserUpdate(
        String firstname,
        String lastname,
        @Email String email,
        String bio,
        String photoUrl
) {
}
