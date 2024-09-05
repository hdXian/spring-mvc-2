package hdxian.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// session management
@Slf4j
@Component
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";

    private static Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    // save session, put cookie into response
    public void createSession(Object value, HttpServletResponse response) {

        String sessionId = UUID.randomUUID().toString();
        log.info("[SessionManager] session created={}, {}", sessionId, value);
        sessionStore.put(sessionId, value);

        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(sessionCookie);
    }

    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME); // sessionId=1234-abcd-5678-...
        if (sessionCookie == null) {
            return null;
        }

        String sessionId = sessionCookie.getValue();
        return sessionStore.get(sessionId); // find in Map<String, Object>
    }

    // remove sessionId from Map
    public void expireSession(HttpServletRequest request) {
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            String sessionId = sessionCookie.getValue();
            sessionStore.remove(sessionId);
        }
    }

    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) { // if request has no cookies
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

}
