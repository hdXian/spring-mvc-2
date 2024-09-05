package hdxian.login.web;

import hdxian.login.domain.member.Member;
import hdxian.login.domain.member.MemberRepository;
import hdxian.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home"; // render home.html
    }

//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        // no login (no cookie named memberId)
        if (memberId == null) {
            return "home";
        }

        // cannot find member (memberId cookie exists)
        Member loginMember = memberRepository.findById(memberId);
        if(loginMember == null) {
            log.info("[HomeController] cannot find member with memberId={}", memberId);
            return "home";
        }

        // success to login (memberId cookie exists)
        model.addAttribute("member", loginMember); // to display member info
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // find session with session ID (returns Member object as value)
        Member loginMember = (Member)sessionManager.getSession(request);

        if (loginMember == null) {
            log.info("[HomeController] loginMember is null: login home rendered.");
            return "home";
        }

        // success to login (memberId cookie exists)
        model.addAttribute("member", loginMember); // to display member info
        return "loginHome";
    }

//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        HttpSession session = request.getSession(false); // if session not exist, return null

        // no login (request has no session)
        if(session == null) {
            return "home";
        }

        // session exists, but no member
        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER); // "loginMember" <- cookie name
        if (loginMember == null) {
            log.info("[HomeController] loginMember is null: render login page.");
            return "home";
        }

        // success to login (session exists)
        model.addAttribute("member", loginMember); // to display member info
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(value = SessionConst.LOGIN_MEMBER, required = false) Member loginMember, Model model) {

        // if loginMember is null (session exist but no member, or session not exist)
        if (loginMember == null) {
            log.info("[HomeController] loginMember is null: render login page.");
            return "home";
        }

        // success to login (session exists)
        model.addAttribute("member", loginMember); // to display member info
        return "loginHome";
    }

}
