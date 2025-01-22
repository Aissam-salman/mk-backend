package app.minkey.fr.minkeybackend.user.service;

import app.minkey.fr.minkeybackend.user.model.Plan;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User updateSub(String custId, Plan plan, Timestamp timestamp) {
        User user = userRepository.findByStripeCustId(custId).orElse(null);
        if (user == null) {
            return null;
        }
        user.setPlan(plan);
        user.setSubscribeAt(timestamp);
        return userRepository.save(user);
    }
}
