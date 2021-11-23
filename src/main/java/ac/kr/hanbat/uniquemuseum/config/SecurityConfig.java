package ac.kr.hanbat.uniquemuseum.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// WebSecurityConfigurerAdapter: 시큐리티 관련 기능을 쉽게 설정하기 위한 클래스 상속
@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //  Bean이란 : Spring IoC 컨테이너가 관리하는 자바 객체를 빈(Bean)이라고 한다.
    //  @Component : 개발자가 직접 작성한 Class를 Bean으로 만드는 것
    //  @Bean : 개발자가 작성한 Method를 통해 반환되는 객체를 Bean으로 만드는 것
    @Bean
    // PasswordEncoder: 비밀번호를 안전하게 저장할 수 있도록 비밀번호의 단방향 암호화를 지원
    PasswordEncoder passwordEncoder() {
        // BCryptPasswordEncoder(): BCrypt 해싱 함수(BCrypt hashing function)를 사용해서 비밀번호를 인코딩해주는 메서드와 사용자의 의해 제출된 비밀번호와 저장소에 저장되어 있는 비밀번호의 일치 여부를 확인
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // permitAll(): 모든 사용자에게 접근 허락, 비로그인 사용자(익명 사용자) 포함
        http.authorizeRequests()
                .antMatchers("/museum/list").permitAll()
                .antMatchers("/museum/read").hasRole("USER")
                .antMatchers("/museum/register").hasRole("ADMIN");


        http.formLogin(); // 인가, 인증에 문제시 로그인 화면으로 이동
        http.csrf().disable(); // CSRF 토큰을 발행하지 않도록 설정
        http.logout() // 로그아웃 페이지로 이동
                .logoutSuccessUrl("/museum/list");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//    }

}
