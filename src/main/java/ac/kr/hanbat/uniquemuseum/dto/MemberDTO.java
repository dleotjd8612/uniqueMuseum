package ac.kr.hanbat.uniquemuseum.dto;

import ac.kr.hanbat.uniquemuseum.entity.MemberRole;
import lombok.*;

import java.util.Set;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private String email; // 회원 이메일
    private String password; // 회원 비밀번호
    private String nickname; // 회원 닉네임
    private boolean fromSocial; // 회원 소셜 로그인 여부
    private Set<MemberRole> roleSet; // 회원 관리
}
