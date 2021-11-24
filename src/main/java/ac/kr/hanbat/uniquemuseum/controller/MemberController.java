package ac.kr.hanbat.uniquemuseum.controller;

import ac.kr.hanbat.uniquemuseum.dto.AuthMemberDTO;
import ac.kr.hanbat.uniquemuseum.dto.MemberDTO;
import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageRequestDTO;
import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.repository.MemberRepository;
import ac.kr.hanbat.uniquemuseum.security.service.ClubUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller // @Controller의 역할은 Model 객체를 만들어 데이터를 담고 View를 찾는 것
@RequestMapping(value = "member") // URL을 컨트롤러의 메서드와 매핑할 때 사용
@Log4j2
@RequiredArgsConstructor // final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MemberController {
    private final ClubUserDetailsService clubUserDetailsService;

    @GetMapping("signup")
    public void signupGet() { // 회원 가입 페이지 호출
        log.info("signup page~~");
    }
    
    @PostMapping("signup")
    public String signupPost(MemberDTO memberDTO, RedirectAttributes redirectAttributes){ // 회원 가입
        log.info("-------------- get signupPost ---------------------");
        log.info("signUp MemberDTO: " + memberDTO);

        String result = clubUserDetailsService.createUser(memberDTO);
        log.info("result: " + result);

        redirectAttributes.addFlashAttribute("result", result);

        return "redirect:/member/signup";
    }
    
    @PostMapping("success")
    public void success() { // 로그인 성공 페이지
        log.info("success....");

    }

    @GetMapping("mypage")
    public void myPageGet(Principal principal, Model model) { // 회원 정보 수정 페이지 호출
        log.info("myPage email: " + principal.getName());

        MemberDTO memberDTO = clubUserDetailsService.getMember(principal.getName());
        log.info("myPage dto: " + memberDTO);
        model.addAttribute("member_dto", memberDTO);
    }
    
    @PostMapping("mypage")
    public String myPagePost(MemberDTO memberDTO, Model model) { // 회원 정보 수정
        log.info("myPage MemberDTO:" + memberDTO);

        Member result = clubUserDetailsService.memberInfoModification(memberDTO);
        log.info(result);

        model.addAttribute("result", result);

        return "redirect:/museum/list";
    }

}
