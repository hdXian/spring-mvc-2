package hdxian.login.domain.login;

import hdxian.login.domain.member.Member;
import hdxian.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     *
     * @return null (if fail to login)
     */
    public Member login(String loginId, String password) {
        // return matched member or null
        return memberRepository.findByLoginId(loginId) // find member with loginId
                .filter(member -> member.getPassword().equals(password)) // check is password matches (if matches, get that member)
                .orElse(null); // or it's null
    }

}
