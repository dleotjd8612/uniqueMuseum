package ac.kr.hanbat.uniquemuseum.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Museum {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno; // 박물관 번호

    @Column(nullable = false)
    private String name; // 이름

    @Column(nullable = false)
    private String address; // 주소

    @Column(nullable = false)
    private String number; // 전화번호

    private String convenienceFacilityInformation; //편의 시설 정보
    private LocalDateTime weekdaysOpen; // 평일 오픈 시간
    private LocalDateTime weekdaysClose; // 평일 클로즈 시간
    private LocalDateTime weekendOpen; // 주말 오픈 시간
    private LocalDateTime weekendClose; // 주말 클로즈 시간
    private String closingInformation; // 휴관 정보
    private Integer adultAdmissionFee; // 성인 관람료
    private Integer teenagerAdmissionFee; // 청소년 관람료
    private Integer childrenAdmissionFee; // 어린이 관람료
    private String admissionFeeInformation; // 관람료 정보
}
