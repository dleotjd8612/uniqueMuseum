package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Museum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MuseumRepository extends JpaRepository<Museum, Long> {

//    @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) " +
//            "from Museum m " +
//            "left outer join Review r on r.museum = m " +
//            "group by m")
//    Page<Object[]> getListPage(Pageable pageable);

    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(distinct r) from Museum m " +
            "left outer join MuseumImage mi on mi.museum = m " +
            "left outer join Review r on r.museum = m group by m")
    Page<Object[]> getListPage(Pageable pageable); // 페이지 처리

    @Query("select m, mi, avg(coalesce(r.grade, 0)), count(r) " +
            "from Museum m left outer join MuseumImage mi on mi.museum = m " +
            "left outer join Review r on r.museum = m " +
            "where m.mno = :mno group by mi")
    List<Object[]> getMuseumWithAll(@Param("mno") Long mno); // 특정 영화 조회

}
