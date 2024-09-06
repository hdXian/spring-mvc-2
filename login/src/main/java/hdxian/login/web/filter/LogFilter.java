package hdxian.login.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

// logging http request
@Slf4j
public class LogFilter implements Filter {

    // servlet filter maintained as singleton. (by servlet container)
    // it's similar to spring singleton bean.

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        UUID uuid = UUID.randomUUID();

        try {
            log.info("[LogFilter] REQUEST [{}][{}]", uuid, requestURI);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("[LogFilter] RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[LogFilter] log filter init");
    }

    @Override
    public void destroy() {
        log.info("[LogFilter] log filter destroyed");
    }

}
