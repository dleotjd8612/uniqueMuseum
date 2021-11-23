package ac.kr.hanbat.uniquemuseum.controller;

import ac.kr.hanbat.uniquemuseum.dto.AuthMemberDTO;
import ac.kr.hanbat.uniquemuseum.dto.MemberDTO;
import ac.kr.hanbat.uniquemuseum.repository.MemberRepository;
import ac.kr.hanbat.uniquemuseum.security.service.ClubUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // @Controller의 역할은 Model 객체를 만들어 데이터를 담고 View를 찾는 것
@RequestMapping("member") // URL을 컨트롤러의 메서드와 매핑할 때 사용
@Log4j2
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MemberController {
    private final ClubUserDetailsService clubUserDetailsService;

    @GetMapping("signup")
    public void signupGet() { // 회원 가입 페이지 호출
        log.info("signup page~~");
    }
    
    @PostMapping("signup")
    public void signupPost(MemberDTO memberDTO){ // 회원 가입
        log.info("-------------------------------------------");
        log.info("authMemberDTO: " + memberDTO);

        clubUserDetailsService.createUser(memberDTO);

        log.info("signupPostDTO: " + memberDTO);
    }
}
