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

    @PostMapping("/google/register")
    public ResponseEntity<?> registerWithGoogle(@RequestBody Map<String, String> body) throws Exception {
        String googleToken = body.get("idToken");

        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + googleToken;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> userInfo = response.getBody();

            User user = userRepository.findByEmail((String) userInfo.get("email")).orElse(null);
            if (user != null) {
                return ResponseEntity.ok(Map.of("status",
                        "existing"));
            }

            User _user = User.builder()
                    .firstname((String) userInfo.get("given_name"))
                    .lastname((String) userInfo.get("family_name"))
                    .email((String) userInfo.get("email"))
                    .googleId((String) userInfo.get("sub"))
                    .role(Role.USER)
                    .build();

        // TODO: add picture in user
           userRepository.save(_user);

            String jwtToken = jwtService.generateToken(new HashMap<>(), _user);
            AuthentificationResponse authResp = AuthentificationResponse.builder()
                    .token(jwtToken)
                    .build();
            return ResponseEntity.ok(authResp);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
        }
    }

    @PostMapping("/google/login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody Map<String, String> body) throws Exception {
        String googleToken = body.get("idToken");

        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + googleToken;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> userInfo = response.getBody();
            String email = (String) userInfo.get("email");
            User user = userRepository.findByEmail(email).orElseThrow();
            var token = jwtService.generateToken(user);

            return ResponseEntity.ok(Map.of("status", "success","token", token));

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google token");
    }

}
