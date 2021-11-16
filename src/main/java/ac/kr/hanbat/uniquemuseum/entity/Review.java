package ac.kr.hanbat.uniquemuseum.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"museum", "member"}) // 추후 member -> clubMember 변경해야 함
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewNum; // 리뷰 번호

    @ManyToOne(fetch = FetchType.LAZY)
    private Museum museum; // 1:N 연관 관계

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member; // 1:N 연관 관계, 추후 ClubMember로 변경해야 함
    
    private int grade; // 리뷰 평점
    private String text; // 리뷰 내용

    public void changeGrade(int grade) { // 리뷰 평점 변경
        this.grade = grade;
    }
    public void changeText(String text) { // 리뷰 내용 변경
        this.text = text;
    }
}
