package hdxian.exception.servlet;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ServletExceptionController {


    @GetMapping("/error-exception")
    public void errEx() {
        throw new RuntimeException("exception occurs!");
    }

    @GetMapping("/error-404")
    public void error404(HttpServletResponse response) throws IOException {
        response.sendError(404, "404 error occurs!");
    }

    @GetMapping("/error-401")
    public void error401(HttpServletResponse response) throws IOException {
        response.sendError(401, "401 error occurs!");
    }

    @GetMapping("/error-500")
    public void error500(HttpServletResponse response) throws IOException {
        response.sendError(500, "500 error occurs!");
    }

}
