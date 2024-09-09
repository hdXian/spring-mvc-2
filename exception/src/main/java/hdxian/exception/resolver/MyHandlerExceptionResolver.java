package hdxian.exception.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            if (ex instanceof IllegalArgumentException) {
                log.info("[MyHandlerExceptionResolver] resolve IllegalArgumentException");

                // 발생한 ex를 여기서 처리하고, sendError() 호출 및 mv 리턴을 통해 정상 흐름으로 변경
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView(); // empty mv -> no rendering
            }

        } catch (IOException e) {
            log.error("[MyHandlerExceptionResolver] IOException occurs", e);
        }

        log.info("[MyHandlerExceptionResolver] failed to resolve [{}]", ex.getClass());
        return null;
    }

}
