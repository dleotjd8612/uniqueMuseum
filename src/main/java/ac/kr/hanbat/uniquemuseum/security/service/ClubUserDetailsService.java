package ac.kr.hanbat.uniquemuseum.security.service;

import ac.kr.hanbat.uniquemuseum.dto.AuthMemberDTO;
import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service // 자동으로 스프링에서 빈으로 처리
@RequiredArgsConstructor
// UserDetailsService: 스프링 시큐리티에서 자동으로 ClubUserDetailsService 클래스를 UserDetailsService로 인식
// MemberRepository를 주입받을 수 있는 구조로 변경하고 @RequiredArgsConstructor 처리
public class ClubUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);

        // 여기서부터 ClubUserDetailsService와 MemberRepository 연동
        Optional<Member> result = memberRepository.findByEmail(username, false);

        if(!result.isPresent()) {
            throw new UsernameNotFoundException("이메일 혹은 소셜 로그인 여부를 확인하세요.");
        }

        Member member = result.get();

        log.info("-------------------------------");
        log.info(member);

        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
          member.getEmail(),
          member.getPassword(),
          member.isFromSocial(),
          member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet()));

        authMemberDTO.setNickname(member.getNickname());
        authMemberDTO.setFromSocial(member.isFromSocial());

        return authMemberDTO;
    }
}
