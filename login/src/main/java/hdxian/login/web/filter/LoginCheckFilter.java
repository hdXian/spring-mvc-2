package hdxian.login.web.filter;

import hdxian.login.web.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static String[] whiteList = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        try {
            log.info("[LoginCheckFilter] start of login check");

            // 로그인 검증이 필요한 경우
            if (isLoginCheckPath(requestURI)) {

                HttpSession session = httpRequest.getSession(false);

                // 세션이 없거나 해당 세션에 연결된 Member가 없으면 로그인 화면으로 리다이렉트
                // (세션이 없거나, 만료됐거나, 그 세션에 연결된 Member가 없거나(유효하지 않은 세션))
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("[LoginCheckFilter] Uncertified request={}", requestURI);
                    HttpServletResponse httpResponse = (HttpServletResponse) response;
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI); // 지정한 경로로 리다이렉트하도록 응답 설정
                    return; // 이후 필터를 진행하지 않음.
                }

            }

            log.info("[LoginCheckFilter] request passed(whiteList or loginUser)={}", requestURI);
            // 로그인 성공 시 다음 필터로 진행
            chain.doFilter(request, response);

        } catch (Exception e) {
            throw e; // 예외를 톰캣(상위 WAS)까지 전달. 예외 처리를 여기서 끝내는 것은 바람직하지 않음.
        } finally {
            log.info("[LoginFilter] end of login check");
        }

    }

    private static boolean isLoginCheckPath(String requestURI) {
        return !(PatternMatchUtils.simpleMatch(whiteList, requestURI));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
