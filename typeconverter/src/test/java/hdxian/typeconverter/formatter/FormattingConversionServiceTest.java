package hdxian.typeconverter.formatter;

import hdxian.typeconverter.converter.IntegerToStringConverter;
import hdxian.typeconverter.converter.IpPortToStringConverter;
import hdxian.typeconverter.converter.StringToIntegerConverter;
import hdxian.typeconverter.converter.StringToIpPortConverter;
import hdxian.typeconverter.customType.IpPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FormattingConversionServiceTest {

    DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

    @Test
    void formattingConversionService() {
        conversionService.addConverter(new IpPortToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());

        conversionService.addFormatter(new MyNumberFormatter());

        // IpPort -> String
        String converted1 = conversionService.convert(new IpPort("127.0.0.1", 8080), String.class);
        assertThat(converted1).isEqualTo("127.0.0.1:8080");

        // String -> IpPort
        IpPort converted2 = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(converted2).isEqualTo(new IpPort("127.0.0.1", 8080));

        // 1000 -> "1,000"
        String converted3 = conversionService.convert(1000, String.class); // call convert(), but MuNumberFormatter used.
        assertThat(converted3).isEqualTo("1,000");

        // "1,000" -> 1000L
        Long converted4 = conversionService.convert("1,000", Long.class);
        assertThat(converted4).isEqualTo(1000L);
    }

}
