package ac.kr.hanbat.uniquemuseum.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Museum extends BaseEntity{
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
//    @DateTimeFormat(pattern = "hh:mm")
    private LocalTime weekdaysOpen; // 평일 오픈 시간
    private LocalTime weekdaysClose; // 평일 클로즈 시간
    private LocalTime weekendOpen; // 주말 오픈 시간
    private LocalTime weekendClose; // 주말 클로즈 시간
    private String closingInformation; // 휴관 정보
    private Integer adultAdmissionFee; // 성인 관람료
    private Integer teenagerAdmissionFee; // 청소년 관람료
    private Integer childrenAdmissionFee; // 어린이 관람료
    private String admissionFeeInformation; // 관람료 정보
}
