package study.datajpa.repository;

import study.datajpa.domain.Member;

import java.util.List;

//사용자 정의 인터페이스
public interface MemberRepositoryCustom {
    List<Member> findMemberCustom();
}
