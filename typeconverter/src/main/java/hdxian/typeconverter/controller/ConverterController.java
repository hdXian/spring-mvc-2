package hdxian.typeconverter.controller;

import hdxian.typeconverter.customType.IpPort;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute(new IpPort("127.0.0.1", 8080));
        return "converter-view"; // converter-view.html
    }

    @GetMapping("/converter/edit")
    public String converterForm(Model model) {
        IpPort ipPort = new IpPort("127.0.0.1", 8080);
        Form form = new Form(ipPort);
        model.addAttribute("form", form);
        return "converter-form";
    }

    // 쿼리 파라미터에 ipPort=abcd.. 식으로 넘어오는 데이터를 (html Form에 의해)
    // IpPort 타입으로 직접 받는게 아니라 Form 타입의 속성으로서 받을 경우, (Form으로 넘기는 데이터가 여러개면 해당 속성들을 가지는 객체를 ModelAttribute로 받을 수 있음)
    // 스프링은 new Form(); 으로 빈 객체를 만든 후 setter를 사용하는 것으로 보임.
    // 즉 이 경우에는 Form 클래스에 인자를 받지 않는 생성자가 필요함. (since spring framework 6.1)
    // 이전에는 바인딩 방식이 달라서 빈 생성자가 없어도 문제가 없던 것으로 보임.
    @PostMapping("/converter/edit")
    public String converterEdit(@ModelAttribute("form") Form form, Model model) {
        IpPort ipPort = form.getIpPort(); // get IpPort from Form
        model.addAttribute("ipPort", ipPort);
        return "converter-view";
    }

    @Data
    static class Form {
        private IpPort ipPort;

        // need to no args constructor. -> 이거 없으면 바인딩 실패함.
        public Form() {
        }

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }

    }

}
