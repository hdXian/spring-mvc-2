package hdxian.login.web.member;

import hdxian.login.domain.member.Member;
import hdxian.login.domain.member.MemberRepository;
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
@RequestMapping("/members")
public class MemberController {

    private final MemberRepository memberRepository; // auto DI

    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) { // put empty Member object into model. (just for page rendering)
        return "/members/addMemberForm"; // /templates/addMemberForm.html
    }

    @PostMapping("/add")
    public String addMember(@Valid @ModelAttribute("member") Member member, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "/members/addMemberForm";
        }

        memberRepository.save(member);
        return "redirect:/"; // redirect to home
    }

}
