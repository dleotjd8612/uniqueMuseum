package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 차후 클럽멤버로 변경해야 함
    // @EntityGraph: 엔티티의 특정한 속성을 같이 로딩하도록 표시
    // attributePaths: 로딩 설정을 변경하고 싶은 속성의 이름을 배열로 명시
    // type: 어떤 방식으로 적용할 것인지 설정, FETCH: attributePaths 에 설정된 값은 EAGER(즉시로딩)로 처리 그 외는 LAZY(지연로딩)
    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByMuseum(Museum museum);

    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(@Param("member") Member member); // 차후 클럽멤버로 변경해야 함, 회원 삭제
}
