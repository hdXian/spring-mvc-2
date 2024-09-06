package hdxian.login;

import hdxian.login.domain.item.Item;
import hdxian.login.domain.item.ItemRepository;
import hdxian.login.domain.member.Member;
import hdxian.login.domain.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        itemRepository.save(new Item("testItem1", 10000, 11));
        itemRepository.save(new Item("testItem2", 20000, 22));

        // add test account
        Member tester = new Member();
        tester.setLoginId("test");
        tester.setPassword("test!");
        tester.setName("tester");
        memberRepository.save(tester);
    }

}
