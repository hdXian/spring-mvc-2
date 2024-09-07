package hdxian.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

// it's a spring boot utility to use servlet error page comfortably. -> need to register as a spring bean.
//@Component // disable to use BasicErrorController (spring boot offers)
public class WebServerExceptionCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 서블릿 컨테이너에서 오류가 발생했을 때 해당 ErrorPage에 설정된 URL을 호출. (웹 요청과 같은 과정으로 동작함. 컨트롤러가 받음.)
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404"); // when response.sendError(404, ..)
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500"); // when response.sendError(RuntimeException.class, ..)

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }

}
