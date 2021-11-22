package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.entity.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMember() { // 사용자 추가
        Member member = Member.builder()
                .email("admin@test.com")
                .password(passwordEncoder.encode("1111"))
                .nickname("관리자")
                .fromSocial(false)
                .build();
        member.addMemberRole(MemberRole.USER);
        member.addMemberRole(MemberRole.MANAGER);
        member.addMemberRole(MemberRole.ADMIN);
        memberRepository.save(member);
    }

    @Test
    public void insertMembers() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@hanbat.ac.kr")
                    .password(passwordEncoder.encode("1111"))
                    .nickname("사용자" + i)
                    .fromSocial(false)
                    .build();

            member.addMemberRole(MemberRole.USER);
            memberRepository.save(member);
        });
    }

    @Commit
    @Transactional
    @Test
    public void testDeleteMember() { // ReviewRepository에서 @Query로 처리(deleteByMember());
        String email = "r100@hanbat.ac.kr";

        Member member = Member.builder().email(email).build();

        // r100@hanbat.ac.kr 사용자가 작성한 댓글 삭제 후 회원 삭제, 순서 주의
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(email);
    }
}
