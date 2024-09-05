package hdxian.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository // spring bean
public class MemberRepository {

    // no concurrency considered. just example code.
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    public Member save(Member member) {
        member.setId(++sequence);
        log.info("[MemberRepository] save: member={}", member); // member.toString (from @Data in class Member)
        store.put(member.getId(), member);
        return member;
    }

    public Optional<Member> findByLoginId(String loginId) {
        return store.values().stream() // loop in values
                .filter(member -> member.getLoginId().equals(loginId)) // find matched elements
                .findFirst(); // get first element in the matches
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

}
