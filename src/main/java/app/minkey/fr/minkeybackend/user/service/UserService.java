package app.minkey.fr.minkeybackend.user.service;

import app.minkey.fr.minkeybackend.dto.UserUpdate;
import app.minkey.fr.minkeybackend.user.model.Plan;
import app.minkey.fr.minkeybackend.user.model.User;
import app.minkey.fr.minkeybackend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public User updateUserInfo(String email, UserUpdate userUpdate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (userUpdate.firstname() != null && !userUpdate.firstname().trim().isEmpty() &&
                !userUpdate.firstname().equals(user.getFirstname())) {
            user.setFirstname(userUpdate.firstname().trim());
        }

        if (userUpdate.lastname() != null && !userUpdate.lastname().trim().isEmpty() &&
                !userUpdate.lastname().equals(user.getLastname())) {
            user.setLastname(userUpdate.lastname().trim());
        }

        if (userUpdate.email() != null && !userUpdate.email().trim().isEmpty() &&
                !userUpdate.email().equals(user.getEmail())) {
            user.setEmail(userUpdate.email().trim());
        }

        if (userUpdate.bio() != null && !userUpdate.bio().trim().isEmpty() &&
                !userUpdate.bio().equals(user.getBio())) {
            user.setBio(userUpdate.bio().trim());
        }

        if (userUpdate.photoUrl() != null && !userUpdate.photoUrl().trim().isEmpty() &&
                !userUpdate.photoUrl().equals(user.getPhotoUrl())) {
            user.setPhotoUrl(userUpdate.photoUrl().trim());
        }

        return userRepository.save(user);
    }
}
