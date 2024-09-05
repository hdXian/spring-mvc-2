package hdxian.login.web.login;

import hdxian.login.domain.login.LoginService;
import hdxian.login.domain.member.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/login/loginForm";
    }

    @PostMapping
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        // if there are failure with binding
        if (bindingResult.hasErrors()) {
            log.info("[LoginController] bindingResult={}", bindingResult);
            return "/login/loginForm";
        }

        // check loginId and password
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("[LoginController] loginMember ? {}", loginMember);

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/login/loginForm";
        }

        // login success logic
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId())); // add memberId as Cookie
        response.addCookie(idCookie); // cookie with no max-age: removed when browser off (session cookie)
        return "redirect:/";
    }

}
