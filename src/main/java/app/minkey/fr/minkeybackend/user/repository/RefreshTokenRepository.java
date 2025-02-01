package app.minkey.fr.minkeybackend.user.repository;

import app.minkey.fr.minkeybackend.user.model.RefreshToken;
import app.minkey.fr.minkeybackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
