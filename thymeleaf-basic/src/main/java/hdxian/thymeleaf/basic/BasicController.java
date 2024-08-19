package hdxian.thymeleaf.basic;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
