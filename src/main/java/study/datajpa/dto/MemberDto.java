package study.datajpa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import study.datajpa.domain.Member;

@Getter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String userName;
    private int age;
    private String teamName;

    public MemberDto(Long id, String userName, String teamName){
        this.id = id;
        this.userName = userName;
        this.teamName = teamName;
    }

    public MemberDto(Member member){
        this.id = member.getId();
        this.userName = member.getUsername();
        this.age = member.getAge();
    }
}
