package hdxian.exception.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("[LogFilter] REQUEST uuid[{}] dispatcherType[{}] requestURI[{}]", uuid, request.getDispatcherType(), requestURI);
            chain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.info("[LogFilter] catch Exception, msg={}", e.getMessage());
            throw e;
        } finally {
            log.info("[LogFilter] RESPONSE uuid[{}] dispatcherType[{}] requestURI[{}]", uuid, request.getDispatcherType(), requestURI);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("[LogFilter] LogFilter init");
    }

    @Override
    public void destroy() {
        log.info("[LogFilter] LogFilter destroyed");
    }

}
