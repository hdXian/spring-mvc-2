package hdxian.exception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class ErrorPageController {

    // ErrorPage 추가해주던 customizer가 지금 비활성화돼서 오류 떠도 WAS가 이쪽으로 요청 안 함.

    // declared in RequestDispatcher as constant
    public static final String ERROR_EXCEPTION = "jakarta.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "jakarta.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "jakarta.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "jakarta.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "jakarta.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "jakarta.servlet.error.status_code";

    @RequestMapping("/error-page/404") // 이 경로를 브라우저 주소창에 그대로 쳐도 페이지 띄워주긴 함.
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        printErrorInfo(request);
        return "/error-page/404"; // viewName
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        printErrorInfo(request);
        return "/error-page/500"; // viewName
    }

    // WAS automatically add error information in request attribute
    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex={}", request.getAttribute(ERROR_EXCEPTION)); // stack trace printed
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatcherType= {}", request.getDispatcherType());
    }

}
