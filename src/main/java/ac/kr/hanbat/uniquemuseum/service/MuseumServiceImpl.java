package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.MuseumDTO;
import ac.kr.hanbat.uniquemuseum.dto.MuseumImageDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageRequestDTO;
import ac.kr.hanbat.uniquemuseum.dto.PageResultDTO;
import ac.kr.hanbat.uniquemuseum.entity.*;
import ac.kr.hanbat.uniquemuseum.repository.MuseumImageRepository;
import ac.kr.hanbat.uniquemuseum.repository.MuseumRepository;
import ac.kr.hanbat.uniquemuseum.repository.ReviewRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service // 해당 클래스를 루트 컨테이너에 빈(Bean) 객체로 생성
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입, 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성
public class MuseumServiceImpl implements MuseumService {
    // JPA 사용을 위한 JPARepogitory 인터페이스의 구현 클래스 타입의 변수를 선언
    private final MuseumRepository museumRepository; // 반드시 final로 선언
    private final MuseumImageRepository museumImageRepository;
    private final ReviewRepository reviewRepository;

    @Transactional // 해당 메서드를 하나의 트랜잭션으로 처리하라는 의미, no Session 발생 시 데이터베이스와 연결 생성
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
    public PageResultDTO<MuseumDTO, Object[]> getList(PageRequestDTO pageRequestDTO) { // 박물관 목록 리스트
//        Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());
//
//        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO); // 검색 조건 처리
        // Querydsl 사용
//        Page<Object[]> result = museumRepository.getListPage(pageable);

        Page<Object[]> result = museumRepository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("mno").descending()));

        // Object[]: 박물관, 박물관 이미지 리스트, 평점 평균, 리뷰 개수의 객체들을 DTO 타입으로 변환
        // asList(): 일반 배열을 arrayList로 변환
        Function<Object[], MuseumDTO> fn = (arr -> entitiesToDTO(
                (Museum) arr[0],
                (List<MuseumImage>) (Arrays.asList((MuseumImage)arr[1])),
                (Double) arr[2],
                (Long) arr[3]
        ));
        log.info(fn);

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MuseumDTO getMuseum(Long mno) {
        List<Object[]> result = museumRepository.getMuseumWithAll(mno);
        Museum museum = (Museum) result.get(0)[0]; // Museum Entity는 가장 앞에 존재 - 모든 Row가 동일한 값
        List<MuseumImage> museumImageList = new ArrayList<>(); // 박물관 이미지 개수만큼 객체 필요
        result.forEach(arr -> {
            MuseumImage museumImage = (MuseumImage) arr[1];
            museumImageList.add(museumImage);
        });
        Double avg = (Double) result.get(0)[2]; // 평균 평점 - 모든 행이 동일한 값
        Long reviewCnt = (Long) result.get(0)[3]; // 리뷰 개수 - 모든 행이 동일한 값

        return entitiesToDTO(museum, museumImageList, avg, reviewCnt);
    }

    @Transactional
    @Override
    public void remove(Long mno) { // 박물관 삭제
        log.info("mno: " + mno);

        reviewRepository.deleteByReviews(mno); // 박물관 삭제 전 리뷰들 삭제
        museumImageRepository.deleteByImages(mno); // 박물관 삭제 전 박물관 이미지들 삭제
        museumRepository.deleteById(mno); // 박물관 삭제
    }

    @Transactional
    @Override
    public void modify(MuseumDTO museumDTO) {
        // JPA를 이용하여 파라미터로 넘어온 MuseumDTO 객체 안에 있는 mno를 가진 데이터를 Museum Entity 클래스 변수에 저장
        log.info(museumDTO.getMno());
        // JPA를 이용하여 파라미터로 넘오온 MuseumDTO 객체 안에 있는 mno를 가진 데이터를 Museum Entity 클래스 변수에 저장
        museumImageRepository.deleteByImages(museumDTO.getMno()); // 기존 이미지들 삭제

        Map<String, Object> entityMap = dtoToEntity(museumDTO);
        Museum museum = (Museum) entityMap.get("museum");
        List<MuseumImage> museumImageList = (List<MuseumImage>) entityMap.get("imgList");
        log.info("museum1: " + museum);
        log.info("museumImage1: " + museumImageList);

        museumRepository.save(museum);
        museumImageList.forEach(museumImage -> {
            museumImageRepository.save(museumImage);
        });
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO) { //Querydsl 처리
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QMuseum qMuseum = QMuseum.museum;
        QReview qReview = QReview.review;
        QMember qMember = QMember.member;

        BooleanExpression expression = qMuseum.mno.gt(0L); // gno > 0
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0) { // 검색 조건이 없는 경우
            return  booleanBuilder;
        }

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("n")) { // 박물관 이름
            conditionBuilder.or(qMuseum.name.contains(keyword));
        }
        if(type.contains("a")) { // 박물관 주소
            conditionBuilder.or(qMuseum.address.contains(keyword));
        }
        if(type.contains("i")) { // 회원 닉네임
            conditionBuilder.or(qMember.nickname.contains(keyword));
        }
        if(type.contains("t")) { // 리뷰 내용
            conditionBuilder.or(qReview.text.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;
    }
}
