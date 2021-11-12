package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.MuseumImage;
import ac.kr.hanbat.uniquemuseum.repository.MuseumImageRepository;
import ac.kr.hanbat.uniquemuseum.repository.MuseumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

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
}
