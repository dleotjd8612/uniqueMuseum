package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.ReviewDTO;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.Review;
import ac.kr.hanbat.uniquemuseum.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;

    @Override
    public List<ReviewDTO> getListOfMovie(Long mno) { // 박물관 1개의 모든 리뷰를 가져옴
        Museum museum = Museum.builder().mno(mno).build(); // 특정(mno) 박물관의 객체를 가져옴
        List<Review> result = reviewRepository.findByMuseum(museum); // DB에서 특정 박물관의 데이터를 가져옴, 박물관 1개에 여러개의 댓글이 있을 수 있기 때문에 List<>로 받음
        // map은 요소들을 특정조건에 해당하는 값으로 변환, filter는 요소들을 조건에 따라 걸러내는 작업, sorted는 요소들을 정렬해주는 작업
        // 요소들의 가공이 끝났다면 리턴해줄 결과를 collect 를 통해 만들어줍니다.
        return result.stream().map(museumReview -> entityToDto(museumReview)).collect(Collectors.toList());
    }

    @Override
    public Long register(ReviewDTO museumReviewDTO) { // 박물관의 리뷰를 추가
        Review museumReview = dtoToEntity(museumReviewDTO); // 사용자가 등록한 댓글의 데이터를 DB에 넣기 위해 Entity로 변환
        reviewRepository.save(museumReview); // 변환된 Entity 객체를 DB에 저장
        return museumReview.getReviewNum(); // DB에 저장된 댓글의 번호만 돌려줌
    }

    @Override
    public void modify(ReviewDTO museumReviewDTO) {
        // findById: review 테이블에서 리뷰 번호(ID)를 이용하여 데이터 조회(Hibernate가 쿼리문 날려줌)
        // Optional<>: Optional 객체를 사용하면 예상치 못한 NullPointerException 예외를 제공되는 메소드로 간단히 회피
        Optional<Review> result = reviewRepository.findById(museumReviewDTO.getReviewNum());

        if(result.isPresent()) { // 박물관에 댓글이 존재하다면...
            Review museumReview = result.get(); // 댓글의 객체를 가져옴
            museumReview.changeGrade(museumReview.getGrade()); // 사용자가 새로 변경한 평점을 변경함
            museumReview.changeText(museumReview.getText()); // 사용자가 새로 변경한 댓글 내용을 변경함
            
            reviewRepository.save(museumReview); // DB에 변경된 데이터 수정
        }
    }

    @Override
    public void remove(Long reviewNum) {
//        reviewRepository.delete("삭제할 Entity 객체");
        reviewRepository.deleteById(reviewNum); // 리뷰 번호를 이용하여 DB에서 댓글 삭제
    }
}
