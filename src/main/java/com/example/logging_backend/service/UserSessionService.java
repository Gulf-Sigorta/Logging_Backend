import com.example.logging_backend.model.Auth.Auth;
import com.example.logging_backend.model.UserSession;
import com.example.logging_backend.repository.UserSessionRepository;
import com.example.logging_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private AuthService authService;

    public void recordLogin(String username) {
        Auth user = authService.findByUsername(username);
        UserSession session = new UserSession();
        session.setUser(user);
        session.setLoginTime(LocalDateTime.now());
        session.setIsActive(true);
        userSessionRepository.save(session);
    }

    public void recordLogout(String username) {
        Auth user = authService.findByUsername(username);
        Optional<UserSession> sessionOpt = userSessionRepository
                .findTopByUserAndIsActiveTrueOrderByLoginTimeDesc(user);

        if (sessionOpt.isPresent()) {
            UserSession session = sessionOpt.get();
            session.setLogoutTime(LocalDateTime.now());
            session.setSessionDuration(Duration.between(session.getLoginTime(), session.getLogoutTime()));
            session.setIsActive(false);
            userSessionRepository.save(session);
        }
    }
}
