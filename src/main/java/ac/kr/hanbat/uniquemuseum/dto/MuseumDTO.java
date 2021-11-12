package ac.kr.hanbat.uniquemuseum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuseumDTO {
    private Long mno; // 박물관 번호
    private String name; // 박물관 이름
    private String address; // 주소
    private String number; // 전화번호
    private String convenienceFacilityInformation; //편의 시설 정보
    private String weekdaysOpen; // 평일 오픈 시간
    private String weekdaysClose; // 평일 클로즈 시간
    private String weekendOpen; // 주말 오픈 시간
    private String weekendClose; // 주말 클로즈 시간
    private String closingInformation; // 휴관 정보
    private Integer adultAdmissionFee; // 성인 관람료
    private Integer teenagerAdmissionFee; // 청소년 관람료
    private Integer childrenAdmissionFee; // 어린이 관람료
    private String admissionFeeInformation; // 관람료 정보

    // 박물관 이미지들...
    @Builder.Default
    private List<MuseumImageDTO> imageDTOList = new ArrayList<>();
}
