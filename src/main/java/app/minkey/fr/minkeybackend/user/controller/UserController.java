package app.minkey.fr.minkeybackend.user.controller;

import app.minkey.fr.minkeybackend.dto.PlanRequest;
import app.minkey.fr.minkeybackend.dto.UserResponse;
import app.minkey.fr.minkeybackend.user.model.Plan;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getFirstname() + " " + user.getLastname())
                .stripeCustId(user.getStripeCustId())
                .photoUrl(user.getPhotoUrl())
                .plan(user.getPlan())
                .build();
        return ResponseEntity.ok(userResponse);
    }


    @PutMapping("/edit")
    public ResponseEntity<UserResponse> editProfile(@RequestBody PlanRequest planRequest) {
        String email = planRequest.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        user.setStripeCustId(planRequest.stripeCustId());
        user.setPlan(Plan.valueOf(planRequest.plan()));
        userRepository.save(user);
        UserResponse userResponse = UserResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .build();
        return ResponseEntity.ok(userResponse);
    }
}
