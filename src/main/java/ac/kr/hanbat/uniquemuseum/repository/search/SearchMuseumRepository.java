package ac.kr.hanbat.uniquemuseum.repository.search;

import ac.kr.hanbat.uniquemuseum.entity.Museum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchMuseumRepository {
    Museum search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
