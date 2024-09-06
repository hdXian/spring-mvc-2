package hdxian.login;

import hdxian.login.web.filter.LogFilter;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    // register logFilter as spring bean
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter()); // set filter
        filterRegistrationBean.setOrder(1); // first
        filterRegistrationBean.addUrlPatterns("/*"); // mapping all urls
        return filterRegistrationBean;
    }

}
