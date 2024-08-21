package hdxian.thymeleaf.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/template")
public class TemplateController {

    @GetMapping("/fragment")
    public String fragment() {
        // fragmentMain.html calls footer.html by th:replace or th:insert
        return "/template/fragment/fragmentMain";
    }

}
