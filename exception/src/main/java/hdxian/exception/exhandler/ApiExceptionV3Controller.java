package hdxian.exception.exhandler;

import hdxian.exception.customexception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV3Controller {

    // for test scope of @ControllerAdvice

    @GetMapping("/api3/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("invalid Member");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 memberId 입력");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }

    // ResponseStatusException는 @ResponseStatus가 붙어있어 ResponseStatusExceptionResolver의 대상이기도 하나,
    // ExceptionHandlerExceptionResolver가 우선순위가 더 높아 exHandle()이 실행됨.
    @GetMapping("/api3/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberid;
        private String name;
    }
}
