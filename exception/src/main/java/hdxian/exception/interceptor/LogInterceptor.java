package hdxian.exception.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        request.setAttribute(LOG_ID, uuid);

        log.info("[LogInterceptor][preHandle] REQUEST uuid=[{}], dispatcherType=[{}], requestURI=[{}], handler=[{}]", uuid, request.getDispatcherType(), requestURI, handler);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("[LogInterceptor][postHandle] mv=[{}]", modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String uuid = (String) request.getAttribute(LOG_ID);
        String requestURI = request.getRequestURI();
        log.info("[LogInterceptor][afterCompletion] RESPONSE uuid=[{}], dispatcherType=[{}], requestURI=[{}]", uuid, request.getDispatcherType(), requestURI);
        if (ex != null) {
            log.error("[afterCompletion] Exception occurs!");
        }
    }

}
