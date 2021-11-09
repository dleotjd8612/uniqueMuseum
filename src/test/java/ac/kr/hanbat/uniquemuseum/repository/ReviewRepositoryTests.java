package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Member;
import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.Review;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMuseumReviews() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Long mno = (long)(Math.random() * 100) + 1;
            Member member = Member.builder().email("r" + i + "@hanbat.ac.kr").build();

            Review review = Review.builder()
                    .member(member)
                    .museum(Museum.builder().mno(mno).build())
                    .grade((int)(Math.random() * 5) + 1)
                    .text("이 영화에 대한 느낌이랄까.." + i)
                    .build();

            reviewRepository.save(review);
        });
    }

    @Test
    public void testGetMuseumReviews() {
        Museum museum = Museum.builder().mno(98L).build();

        List<Review> result = reviewRepository.findByMuseum(museum);

        result.forEach(museumReview -> {
            System.out.println(museumReview.getReviewNum());
            System.out.println("\t" + museumReview.getGrade());
            System.out.println("\t" + museumReview.getText());
            System.out.println("\t" + museumReview.getMember().getEmail());
            System.out.println("----------------------------------------");
        });

    }
}
