package hdxian.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    // NumberFormat.parse (String -> Number)
    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("[MyNumberFormatter] parse text={}, locale={}", text, locale);
        NumberFormat nf = NumberFormat.getInstance(locale);
        return nf.parse(text);
    }

    // NumberFormat.format (Number -> String)
    @Override
    public String print(Number object, Locale locale) {
        log.info("[MyNumberFormatter] print object={}, locale={}", object, locale);
        NumberFormat nf = NumberFormat.getInstance(locale);
        return nf.format(object);
    }

}
