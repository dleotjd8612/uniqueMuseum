package ac.kr.hanbat.uniquemuseum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // 자동으로 시간 처리, 엔티티 객체의 변화를 감지, AuditingEntityListener.class 사용 여부 설정
public class UniqueMuseumApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniqueMuseumApplication.class, args);
    }

}
