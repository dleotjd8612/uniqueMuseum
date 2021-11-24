package ac.kr.hanbat.uniquemuseum.controller;

import ac.kr.hanbat.uniquemuseum.dto.ReviewDTO;
import ac.kr.hanbat.uniquemuseum.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController  // @RestController는 단순히 객체만을 반환하고 객체 데이터는 JSON 또는 XML 형식으로 HTTP 응답에 담아서 전송
@RequestMapping("reviews")
@Log4j2
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("permitAll()")
    @GetMapping("/{mno}/all")
    // @PathVariable: URL 경로에 변수를 넣어주는 것
    public ResponseEntity<List<ReviewDTO>> getList(@PathVariable("mno") Long mno) {
        log.info("------------ list ------------");
        log.info("MNO:" + mno);

        List<ReviewDTO> reviewDTOList = reviewService.getListOfMovie(mno);
        log.info(reviewDTOList);
        // ResponseEntity<>: HTTP 요청(Request) 또는 응답(Response)에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{mno}")
    // @RequestBody: HTTP 요청 몸체를 자바 객체로 변환하고 자바 객체를 HTTP 응답 몸체로 변환
    public ResponseEntity<Long> addReview(@RequestBody ReviewDTO museumReviewDTO) {
        log.info("---------- add museumReview ------------");
        log.info("reviewDTO: " + museumReviewDTO);

        Long reviewnum = reviewService.register(museumReviewDTO);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> modifyReview(@PathVariable Long reviewnum, @RequestBody ReviewDTO museumReviewDTO) {
        log.info("------------ modify MuseumReview ------------");
        log.info("reviewDTO: " + museumReviewDTO);
        reviewService.modify(museumReviewDTO);
        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{mno}/{reviewnum}")
    public ResponseEntity<Long> removeReview(@PathVariable Long reviewnum) {
        log.info("------------- modify removeReview -----------");
        log.info("reviewnum: " + reviewnum);

        reviewService.remove(reviewnum);

        return new ResponseEntity<>(reviewnum, HttpStatus.OK);
    }
}
