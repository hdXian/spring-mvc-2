package hdxian.typeconverter;

import hdxian.typeconverter.converter.IntegerToStringConverter;
import hdxian.typeconverter.converter.IpPortToStringConverter;
import hdxian.typeconverter.converter.StringToIntegerConverter;
import hdxian.typeconverter.converter.StringToIpPortConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // add type Converters on ConversionService
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    }

}
