package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    // 이메일 기준으로 조회하고 소셜 로그인 사용자를 구분
    // @EntityGraph는 JPA가 어떤 엔티티를 불러올 때 이 엔티티와 관계된 엔티티를 불러올 것인지에 대한 정보를 제공, 쿼리 메소드 마다 연관 관계의 Fetch 모드를 유연하게 설정
    // @EntityGraph를 이용해서 left outer join으로 MemberRole 처리
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial = :social and m.email = :email")
    // 사용자의 이메일과 소셜로 추가된 회원 여부를 선택해서 동작
    Optional<Member> findByEmail(@Param("email") String email, @Param("social") boolean social);
}
