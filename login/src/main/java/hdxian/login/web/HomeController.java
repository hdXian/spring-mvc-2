package hdxian.login.web;

import hdxian.login.domain.member.Member;
import hdxian.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberRepository memberRepository;

//    @GetMapping("/")
    public String home() {
        return "home"; // render home.html
    }

    @GetMapping("/")
    public String loginHome(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

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

}
