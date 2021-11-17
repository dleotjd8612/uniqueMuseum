package ac.kr.hanbat.uniquemuseum.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long reviewNum; // 리뷰 번호
    private Long mno; // 박물관 번호

    private String nickname; // 회원 닉네임
    private String email; // 회원 이메일
    
    private int grade; // 리뷰 평점
    private String text; // 리뷰 내용
    private LocalDateTime regDate, modDate; // 리뷰 등록 시간 및 수정 시간
}
