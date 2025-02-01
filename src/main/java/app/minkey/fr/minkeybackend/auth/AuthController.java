package app.minkey.fr.minkeybackend.auth;

import app.minkey.fr.minkeybackend.dto.AuthenticationRequest;
import app.minkey.fr.minkeybackend.dto.AuthentificationResponse;
import app.minkey.fr.minkeybackend.dto.ForgotReq;
import app.minkey.fr.minkeybackend.dto.RegisterRequest;
import app.minkey.fr.minkeybackend.mail.BrevoService;
import app.minkey.fr.minkeybackend.mail.BrevoTemplate;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;
    private final BrevoService brevoService;

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<AuthentificationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<AuthentificationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody  String request) {
        ForgotReq forgotReq = ForgotReq.builder()
                .email(request)
                .build();
        User user = userService.getUserByEmail(forgotReq.email());
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        var token = jwtService.generateToken(user);

        BrevoTemplate brevoTemplate = new BrevoTemplate(1, Map.of("url","http://localhost:3000/login", "token", token));

        brevoService.sendBrevoMail(brevoTemplate, user.getEmail());
        return ResponseEntity.ok("email send");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (refreshToken == null || refreshToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }

        try {
            String newAccessToken = jwtService.refreshAccessToken(refreshToken);
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
