package hdxian.login.web.interceptor;

import hdxian.login.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();
        log.info("[LoginCheckInterceptor.preHandle] login check interceptor called: {}", requestURI);

        HttpSession session = request.getSession();
        if(session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("[LoginCheckInterceptor.preHandle] failed to check login: {}", requestURI);

            // 로그인 창을 띄우고 튕겨져 나온 URI를 넣어줌.
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false; // 다음으로 진행하지 않음. 그냥 여기까지의 상태로 응답. preHandle에서 멈췄으므로 컨트롤러도 호출 x.
        }

        // 다음으로 진행
        return true;
    }

}
