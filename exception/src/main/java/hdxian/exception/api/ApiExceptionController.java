package hdxian.exception.api;

import hdxian.exception.customexception.BadRequestException;
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
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
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

    @GetMapping("/api/response-status-ex1")
    public String responseStatusEx1() {
        // @ResponseStatus를 붙인 Exception 클래스
        throw new BadRequestException();
    }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        // @ResponseStatus를 별도로 붙일 수 없을 때 (라이브러리 예외 등) ResponseStatusException 객체를 직접 throw
        // 둘 다 ResponseStatusExceptionResolver가 처리함.
        // reason (오류 메시지)에 메시지 소스 사용 가능. (없으면 넣어둔 문자열 자체를 defaultMessage로 지정)
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberid;
        private String name;
    }

}
