package hdxian.login.web.login;

import hdxian.login.domain.login.LoginService;
import hdxian.login.domain.member.Member;
import hdxian.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "/login/loginForm";
    }

//    @PostMapping("/login")
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

    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute("loginForm") LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        // if there are failure in binding
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

        // login success -> create session
        sessionManager.createSession(loginMember, response); // uuid와 loginMember 쌍으로 세션을 저장하고, 쿠키를 만들어 response에 추가해줌.
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // expire cookie and redirect to home
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        // request의 cookies에서 sessionId를 찾고, 그걸 key로 하는 세션 저장소에서 세션 삭제
        sessionManager.expireSession(request);
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie idCookie = new Cookie(cookieName, null);
        idCookie.setMaxAge(0); // how to expire cookies -> set max-age to 0(zero).
        response.addCookie(idCookie);
    }

}
