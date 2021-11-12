package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageRequestDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageResultDTO;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.MuseumImage;
import ac.kr.hanbat.uniquemuseum.repository.MuseumImageRepository;
import ac.kr.hanbat.uniquemuseum.repository.MuseumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MuseumServiceImpl implements MuseumService {
    private final MuseumRepository museumRepository;
    private final MuseumImageRepository museumImageRepository;

    @Transactional
    @Override
    public Long register(MuseumDTO museumDTO) { // 박물관 등록
        Map<String, Object> entityMap = dtoToEntity(museumDTO);
        Museum museum = (Museum) entityMap.get("museum");
        List<MuseumImage> museumImageList = (List<MuseumImage>) entityMap.get("imgList");

        museumRepository.save(museum);
        museumImageList.forEach(museumImage -> {
            museumImageRepository.save(museumImage);
        });
        return museum.getMno();
    }

    @Override
    public PageResultDTO<MuseumDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());

        Page<Object[]> result = museumRepository.getListPage(pageable);

        Function<Object[], MuseumDTO> fn = (arr -> entitiesToDTO(
                (Museum) arr[0],
                (List<MuseumImage>) (Arrays.asList((MuseumImage)arr[1])),
                (Double) arr[2],
                (Long) arr[3]
        ));

        return new PageResultDTO<>(result, fn);
    }
}
