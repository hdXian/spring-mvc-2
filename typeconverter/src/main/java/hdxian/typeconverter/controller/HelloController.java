package hdxian.typeconverter.controller;

import hdxian.typeconverter.customType.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String NumberFormatExHandle(NumberFormatException ex) {
        System.out.println("ex = " + ex);
        return "failed to parse data";
    }

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data"); // http request parameter is always string.
        Integer intVal = Integer.valueOf(data); // it's need to parse to other type.
        System.out.println("intVal = " + intVal);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam("data") Integer data) {
        // Spring converts query string automatically by using TypeConverter
        // @ModelAttribute, @PathVariable are the same as well.
        System.out.println("data = " + data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam("ipPort") IpPort ipPort) {
        // ArgumentResolver uses ConversionService when generate arguments.
        // such as @RequestParam, @ModelAttribute, ...
        System.out.println("ipPort = " + ipPort);
        return "ok";
    }

}
