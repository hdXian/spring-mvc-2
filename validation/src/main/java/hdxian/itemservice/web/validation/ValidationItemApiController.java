package hdxian.itemservice.web.validation;

import hdxian.itemservice.web.validation.form.ItemAddForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@Validated @RequestBody ItemAddForm form, BindingResult bindingResult) {
        log.info("controller called");

        if (bindingResult.hasErrors()) {
            log.info("validation error occurs: {}", bindingResult);
            return bindingResult.getAllErrors(); // returns object (@ResponseBody)
        }

        log.info("success logic processed");
        return form; // returns object (@ResponseBody)
    }

}
