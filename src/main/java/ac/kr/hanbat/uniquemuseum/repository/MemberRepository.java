package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

}
