package ac.kr.hanbat.uniquemuseum.repository;

import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.MuseumImage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class MeseumRepositoryTests {
    @Autowired
    private MuseumRepository museumRepository;
    @Autowired
    private MuseumImageRepository museumImageRepository;

    @Commit
    @Transactional
    @Test
    public void insertMuseum() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Museum museum = Museum.builder()
                    .name("박물관 이름 " + i)
                    .address("대전광역시 어디어디 " + i)
                    .number("042-555-1234")
                    .convenienceFacilityInformation("기념품 판매점" + i)
                    .weekdaysOpen(LocalTime.of(9, 0))
                    .weekdaysClose(LocalTime.of(18, 0))
                    .weekendOpen(LocalTime.of(9, 0))
                    .weekendClose(LocalTime.of(18, 0))
                    .closingInformation("없음")
                    .adultAdmissionFee(10000)
                    .teenagerAdmissionFee(8000)
                    .childrenAdmissionFee(5000)
                    .admissionFeeInformation("국가유공자 및 장애인 무료")
                    .build();

            System.out.println("------------------------------------");

            museumRepository.save(museum);

            int count = (int)(Math.random() * 5) + 1;

            for(int j = 0; j < count; j++) {
                MuseumImage museumImage = MuseumImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .museum(museum)
                        .imgName("테스트" + j + ".jpg")
                        .build();

                museumImageRepository.save(museumImage);
            }
        });
    }

//    @Test
//    public void testListPage() {
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
//        Page<Object[]> result = museumRepository.getListPage(pageRequest);
//        for(Object[] objects : result.getContent()) System.out.println(Arrays.toString(objects));
//    }

//    @Test
//    public void testGetMuseumWithAll() {
//        List<Object[]> result = museumRepository.getMuseumWithAll(100L);
//
//        System.out.println(result);
//
//        for(Object[] arr : result) System.out.println(Arrays.toString(arr));
//    }
}
