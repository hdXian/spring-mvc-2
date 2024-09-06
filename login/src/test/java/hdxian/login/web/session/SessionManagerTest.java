package hdxian.login.web.session;

import hdxian.login.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.*;

public class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    public void sessionTest() {
        MockHttpServletResponse response = new MockHttpServletResponse(); // mock http response (for test)
        Member member = new Member();
        // create uuid -> put into store<uuid, member> -> crate cookie (sessionId=123-abd-456-...) -> set cookie on response
        sessionManager.createSession(member, response); // create session, put cookie into response.

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies()); // set cookies as same as response

        Object result = sessionManager.getSession(request); // it will return Member object
        assertThat(result).isEqualTo(member);

        sessionManager.expireSession(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull();
    }

}
