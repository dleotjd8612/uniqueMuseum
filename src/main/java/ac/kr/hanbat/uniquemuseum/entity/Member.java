package ac.kr.hanbat.uniquemuseum.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{
    @Id
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private boolean fromSocial;

    // @ElementCollection: Entity가 아닌 단순한 형태의 객체 집합을 정의하고 관리하는 방법
    // member는 여러 개의 권한을 가질 수 있어야 합니다. 다만 이 권한은 객체의 일부로만 사용되기 때문에 @ElementCollection 이용
    // @Builder.Default: 필드에 기본값 설정 시 사용
    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Column(nullable = false)
    private Set<MemberRole> roleSet = new HashSet<>();

    public void addMemberRole(MemberRole memberRole) { // 계정에 권한 추가
        roleSet.add(memberRole);
    }
}
