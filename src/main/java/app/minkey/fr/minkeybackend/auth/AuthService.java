package app.minkey.fr.minkeybackend.auth;


import app.minkey.fr.minkeybackend.dto.AuthenticationRequest;
import app.minkey.fr.minkeybackend.dto.RegisterRequest;
import app.minkey.fr.minkeybackend.user.model.Plan;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .plan(Plan.USER)
                .stripeCustId(request.getStripeCustomerId())
                .build();
        return userRepository.save(user);

    }


    public void  authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


    }
}
