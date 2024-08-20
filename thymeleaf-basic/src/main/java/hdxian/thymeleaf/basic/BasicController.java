package hdxian.thymeleaf.basic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/basic")
public class BasicController {

    @GetMapping("/text-basic")
    public String textBasic(Model model) {
        String data = "hello <b>Spring!</b>";
        model.addAttribute("data", data);
        return "/basic/text-basic";
    }

    @GetMapping("/variable")
    public String variable(Model model) {
        User userA = new User("userA", 10);
        User userB = new User("userB", 20);

        List<User> userList = new ArrayList<>();
        userList.add(userA);
        userList.add(userB);

        HashMap<String, User> userMap = new HashMap<>();
        userMap.put("userA", userA);
        userMap.put("userB", userB);

        model.addAttribute("user", userA); // add User object on model
        model.addAttribute("userList", userList); // add List on model
        model.addAttribute("userMap", userMap); // add Map on model
        return "/basic/variable";
    }

    @GetMapping("/basic-objects")
    public String basicObjects(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model) {
        session.setAttribute("sessionData", "hello session");
        model.addAttribute("httpSession", session);
        model.addAttribute("request", request);
        model.addAttribute("response", response);
        model.addAttribute("servletContext", request.getServletContext());
        return "/basic/basic-objects";
    }

    @GetMapping("/date")
    public String basicDate(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "/basic/date";
    }

    @GetMapping("/link")
    public String urlLink(Model model) {
        model.addAttribute("param1", "data1");
        model.addAttribute("param2", "data2");
        return "/basic/link";
    }

    @GetMapping("/literal")
    public String literal(Model model) {
        model.addAttribute("data", "hello");
        return "/basic/literal";
    }

    @GetMapping("/operation")
    public String operation(Model model) {
        model.addAttribute("nullData", null);
        model.addAttribute("data", "hello");
        return "/basic/operation";
    }

    @GetMapping("/attribute")
    public String attribute() {
        return "basic/attribute";
    }

    @GetMapping("/each")
    public String each(Model model) {
        addUsers(model);
        return "/basic/each";
    }

    private static void addUsers(Model model) {
        List<User> list = new ArrayList<>();
        list.add(new User("userA", 10));
        list.add(new User("userB", 20));
        list.add(new User("userC", 30));

        model.addAttribute("userList", list);
    }

    @Component("helloBean")
    static class HelloBean {
        public String hello(String data) {
            return "hello " + data;
        }
    }

    @Data
    static class User {
        private String username;
        private int age;

        public User(String userName, int age) {
            this.username = userName;
            this.age = age;
        }

    }

}
