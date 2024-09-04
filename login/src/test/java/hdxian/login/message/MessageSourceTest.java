package hdxian.login.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class MessageSourceTest {

    @Autowired
    MessageSource messageSource;

    // basic usage
    @Test
    void helloMessage() {
        /*
         * getMessage(String code, Object[] args, Locale locale);
         * if Local param is null -> Locale.getDefault() called.
         * my system is ko_KR -> Locale.getDefault() returns Locale.KOREA
         * -> attempts to lookup "messages_ko.properties" -> no such file -> "messages.properties selected"
         */
        String result = messageSource.getMessage("hello", null, null);
        writeLog("helloMessage", result);
        assertThat(result).isEqualTo("안녕");
    }

    // when there are no matched message code in message file
    @Test
    void notFoundMessageCode() {
        // getMessage() throws NoSuchMessageException
        assertThatThrownBy(() -> messageSource.getMessage("non_exist_code", null, null))
                .isInstanceOf(NoSuchMessageException.class);
    }

    // set default message
    @Test
    void defaultMessage() {
        // getMessage(String code, Object[] args, String defaultMessage, Locale locale);
        String result = messageSource.getMessage("non_exist_code", null, "기본 메시지", null);
        assertThat(result).isEqualTo("기본 메시지");
    }

    @Test
    void getMessageWithArgs() {
        String result = messageSource.getMessage("hello.name", new Object[]{"Spring"}, null);
        writeLog("getMessageWithArgs", result);
        // hello.name=안녕 {0} <- objects[0]
        assertThat(result).isEqualTo("안녕 Spring");
    }

    @Test
    void defaultLang() {
        // "messages.properties" selected. (it's written by Korean)
        String result = messageSource.getMessage("hello", null, null);
        writeLog("defaultLang", result);
        assertThat(result).isEqualTo("안녕");
    }

    @Test
    void enLang() {
        // if Local is en, "messages_en.properties" selected
        String result = messageSource.getMessage("hello", null, Locale.ENGLISH);
        writeLog("enLang", result);
        assertThat(result).isEqualTo("hello");
    }

    // custom
    private static void writeLog(String methodName, String result) {
        String s = "result of " + methodName + " = {}";
        log.info(s, result);
    }

}
