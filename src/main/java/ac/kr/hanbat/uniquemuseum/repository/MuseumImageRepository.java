package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.MuseumImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MuseumImageRepository extends JpaRepository<MuseumImage, Long> {

    @Modifying
    @Query("delete from MuseumImage mi where mi.museum.mno = :mno") // 박물관 삭제 전 박물관 이미지들 삭제
    void deleteByImages(@Param("mno") Long mno);

    @Modifying
    @Query("select mi from MuseumImage mi where mi.museum.mno = :mno") // 박물관 이미지 가져오기
    List<MuseumImage> getMuseumImages(@Param("mno") Long mno);
}
