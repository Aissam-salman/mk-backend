package app.minkey.fr.minkeybackend.user.controller;

import app.minkey.fr.minkeybackend.dto.UserResponse;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserRepository userRepository;

    @PostMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .subscribeAt(user.getSubscribeAt())
                .build();
        return ResponseEntity.ok(userResponse);
    }
}
