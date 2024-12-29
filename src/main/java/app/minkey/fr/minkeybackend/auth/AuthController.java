package app.minkey.fr.minkeybackend.auth;

import app.minkey.fr.minkeybackend.config.JwtService;
import app.minkey.fr.minkeybackend.user.model.Role;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @SneakyThrows
    @PostMapping("/register")
    public ResponseEntity<AuthentificationResponse> register(@RequestBody RegisterRequest request){
        return  ResponseEntity.ok(authService.register(request));
    }
    @SneakyThrows
    @PostMapping("/login")
    public ResponseEntity<AuthentificationResponse> login(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authService.authenticate(request));
    }

}
