package hdxian.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

// to generate json error message response
@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
