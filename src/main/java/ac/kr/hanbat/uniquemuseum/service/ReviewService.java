package ac.kr.hanbat.uniquemuseum.service;

import ac.kr.hanbat.uniquemuseum.dto.ReviewDTO;
import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.Review;

import java.util.List;

public interface ReviewService {
    // 박물관의 모든 리뷰를 가져옴
    List<ReviewDTO> getListOfMovie(Long mno);
    // 박물관의 리뷰를 추가
    Long register(ReviewDTO museumReviewDTO);
    // 특정한 박물관 리뷰 수정
    void modify(ReviewDTO museumReviewDTO);
    // 박물관 리뷰 수정
    void remove(Long reviewNum);

    default Review dtoToEntity(ReviewDTO museumReviewDTO) {
        Review museumReview = Review.builder()
                .reviewNum(museumReviewDTO.getReviewNum())
                .museum(Museum.builder().mno(museumReviewDTO.getMno()).build())
                .member(Member.builder().email(museumReviewDTO.getEmail()).build()) // 추후 clubmember로 변경해야 함
                .grade(museumReviewDTO.getGrade())
                .text(museumReviewDTO.getText())
                .build();
        return museumReview;
    }

    default ReviewDTO entityToDto(Review museumReview) {
        ReviewDTO museumReviewDTO = ReviewDTO.builder()
                .reviewNum(museumReview.getReviewNum()) // 리뷰 번호
                .mno(museumReview.getMuseum().getMno()) // 박물관 번호
                .email(museumReview.getMember().getEmail()) // 회원 이메일(ID)
                .nickname(museumReview.getMember().getNickname()) // 회원 닉네임
                .grade(museumReview.getGrade()) // 리뷰 평점
                .text(museumReview.getText()) // 리뷰 내용
                .regDate(museumReview.getRegDate()) // 리뷰 등록 시간
                .modDate(museumReview.getModDate()) // 리뷰 수정 시간
                .build();
        return museumReviewDTO;
    }
}
