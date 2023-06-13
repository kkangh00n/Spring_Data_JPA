package study.datajpa.repository;

import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

    //반환 타입 테스트
    @Test
    public void testReturnType(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        List<Member> memberList = memberRepository.findListByUsername("memberA");
        Member findMember = memberRepository.findMemberByUsername("memberB");
        Optional<Member> memberOptional = memberRepository.findOptionalByUsername("memberA");

        Assertions.assertThat(memberList.size()).isEqualTo(1);
        Assertions.assertThat(findMember).isEqualTo(null);
        Assertions.assertThat(memberOptional.get()).isEqualTo(saveMember);

    }
}