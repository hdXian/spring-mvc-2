package hdxian.login.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            return "no session exist.";
        }

        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session attribute name={}, value={}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("MaxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("CreationTime={}", new Date(session.getCreationTime()));
        log.info("LastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "session information are logged. check logs in console";
    }
}
