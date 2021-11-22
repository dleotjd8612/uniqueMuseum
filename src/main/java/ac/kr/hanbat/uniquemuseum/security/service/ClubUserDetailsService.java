package ac.kr.hanbat.uniquemuseum.security.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Log4j2
@Service // 자동으로 스프링에서 빈으로 처리
// UserDetailsService: 스프링 시큐리티에서 자동으로 ClubUserDetailsService 클래스를 UserDetailsService로 인식
public class ClubUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("ClubUserDetailsService loadUserByUsername " + username);
        return null;
    }
}
