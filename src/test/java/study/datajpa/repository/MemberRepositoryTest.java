package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.dto.MemberDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(saveMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    //반환 타입 테스트
    @Test
    public void testReturnType(){
        Member member = new Member("memberA");
        Member saveMember = memberRepository.save(member);

        List<Member> memberList = memberRepository.findListByUsername("memberA");
        Member findMember = memberRepository.findMemberByUsername("memberB");
        Optional<Member> memberOptional = memberRepository.findOptionalByUsername("memberA");

        assertThat(memberList.size()).isEqualTo(1);
        assertThat(findMember).isEqualTo(null);
        assertThat(memberOptional.get()).isEqualTo(saveMember);

    }

    //Spring Data JPA 페이징 & 정렬
    @Test
    public void paging(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        PageRequest pageRequest = PageRequest.of(0, 3,
                Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findPageByAge(10, pageRequest);
        Slice<Member> slice = memberRepository.findSliceByAge(10, pageRequest);
        List<Member> list = memberRepository.findListByAge(10, pageRequest);
        List<Member> onlySortList = memberRepository.findSortByAge(10, Sort.by(Sort.Direction.DESC, "username"));

        //map 함수를 이용한 DTO 변환
        Page<MemberDto> map = page.map(m -> new MemberDto(m.getId(), m.getUsername(), m.getTeam().getName()));

        //then

        //page
        List<Member> content = page.getContent();                    //현재 페이지의 조회된 데이터
        assertThat(content.size()).isEqualTo(3);            //현재 페이지의 조회된 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5);   //전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0);          //현재 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2);      //전체 페이지 수
        assertThat(page.isFirst()).isTrue();                         //첫번째 항목인가?
        assertThat(page.hasNext()).isTrue();                         //다음 페이지가 있나?

        //slice
        List<Member> sliceContent = slice.getContent();
        assertThat(sliceContent.size()).isEqualTo(3);
//        assertThat(slice.getTotalElements()).isEqualTo(5);          //전체 데이터 조회 불가
        assertThat(slice.getNumber()).isEqualTo(0);
//        assertThat(slice.getTotalPages()).isEqualTo(2);            //전체 데이터 조회 불가
        assertThat(slice.isFirst()).isTrue();
        assertThat(slice.hasNext()).isTrue();

    }

    @Test
    public void bulkUpdate(){
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        //when
        int resultCount = memberRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void queryHint(){
        //given
        memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        //when
        Member member = memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");

        em.flush();     //update 쿼리 실행 X
    }

    @Test
    public void custom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }
}