package ac.kr.hanbat.uniquemuseum.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = "museum")
public class MuseumImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum; // 이미지 번호
    private String uuid; // 이미지 고유번호
    private String imgName; // 이미지 이름
    private String path; // 이미지 경로

    @ManyToOne(fetch = FetchType.LAZY)
    private Museum museum;
}
