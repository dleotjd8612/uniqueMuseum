package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Museum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MuseumRepository extends JpaRepository<Museum, Long> {

    @Query("select m, avg(coalesce(r.grade, 0)), count(distinct r) " +
            "from Museum m " +
            "left outer join Review r on r.museum = m " +
            "group by m")
    Page<Object[]> getListPage(Pageable pageable);
}
