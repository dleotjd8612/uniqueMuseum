package ac.kr.hanbat.uniquemuseum.repository.search;

import ac.kr.hanbat.uniquemuseum.entity.Museum;
import ac.kr.hanbat.uniquemuseum.entity.QMuseum;
import ac.kr.hanbat.uniquemuseum.entity.QMuseumImage;
import ac.kr.hanbat.uniquemuseum.entity.QReview;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

// QuerydslRepositorySupport: Spring Data JPA에 포함된 클래스, Querydsl 라이브러리를 이용해서 직접 무언가를 구현할 때 사용
@Log4j2
public class SearchMuseumRepositoryImpl extends QuerydslRepositorySupport implements SearchMuseumRepository {
    public SearchMuseumRepositoryImpl() {
        // QuerydslRepositorySupport 클래스의 생성자를 이용하여 도메인 클래스의 객체를 생성
        super(Museum.class);
    }

    @Override
    public Museum search1() {
        log.info("search........");

        // QBoard(Q도메인)의 객체를 가져온다.
        QReview review = QReview.review;
        QMuseum museum = QMuseum.museum;

        JPQLQuery<Museum> jpqlQuery = from(museum);
        jpqlQuery.leftJoin(review).on(review.museum.eq(museum));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(museum, review.text);
        tuple.groupBy(museum);

        log.info("-----------------------");
        log.info(tuple);
        log.info("-----------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage.................");

        // Querydsl 처리, Q도메인 클래스 객체를 가져옴
        QReview review = QReview.review;
        QMuseum museum = QMuseum.museum;
        QMuseumImage museumImage = QMuseumImage.museumImage;

        // SQL문 작성, SPQLQuery: 쿼리 메소드나 @Query에서 처리하기 어려운 복잡한 쿼리문 작성 시 사용
        JPQLQuery<Museum> jpqlQuery = from(museum);
        jpqlQuery.leftJoin(museumImage).on(museumImage.museum.eq(museum));
        jpqlQuery.leftJoin(review).on(review.museum.eq(museum));

        // Tuple: 정해진 엔티티 객체 단위가 아니라 각각의 데이터를 추출하는 경우 사용, 여러 테이블의 집합 함수를 처리하는 것
        JPQLQuery<Tuple> tuple = jpqlQuery.select(museum, museumImage, review.grade.avg(), review.countDistinct());

        // BooleanBuilder: 쿼리의 조건 설정인 where뒤의 조건을 생성해주는 것
        // BooleanExpression: 복잡한 동적 쿼리를 표현, QueryDSL Repository의 표현을 좀더 직관적으로 볼 수 있도록 리팩토링하는 과정
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = museum.mno.gt(0L);
        // and(): 생성한 조건을 추가
        booleanBuilder.and(expression);

        if(type != null) {
            // split( )은 공백이 1개이건 2개이건 n개이건 상관없이 무조건 1개로 보고 처리
            // split("")은 공백 1개, 1개를 각각의 공백으로 따로따로 처리
            String[] typeArr = type.split("");

            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for(String t : typeArr){
                switch (t){
                    case "n":
                        // contains(): 함수는 대상 문자열에 특정 문자열이 포함되어 있는지 확인하는 함수
                        conditionBuilder.or(museum.name.contains(keyword));
                        break;
                    case "a":
                        conditionBuilder.or(museum.address.contains(keyword));
                        break;
                    case "t":
                        conditionBuilder.or(review.text.contains(keyword));
                        break;
                }
            }
            // 새로 추가된 조건 추가
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

        // order by, 현재 설정된 정렬 조건을 가져옴
        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            // isAscending(): 현재 설정된 정렬 조건 검사
            Order direction = order.isAscending()? Order.ASC : Order.DESC;
            // getProperty(): 컬럼을 가져온다. (예: order by bno desc면 bno를 가져오는 것)
            String prop = order.getProperty();

            // PathBuilder: Sort 객체의 속성 등을 처리, PathBuilder 객체 생성 시, 문자열로 된 이름은 JPQLQuery를 생성할 때 이용하면 변수명과 동일해야 한다.
            PathBuilder orderByExpression = new PathBuilder(Museum.class, "museum");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
        });
        tuple.groupBy(museum);

        // Page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        // fetch(): 각각의 데이터를 추출한 필드에 대한 페이징 + 정렬 조건의 전체 데이터를 가져옴
        List<Tuple> result = tuple.fetch();

        // fetchCount(): 위에서 추가한 전체 데이터의 개수를 가져옴
        long count = tuple.fetchCount();
        log.info("개수: " + count);

        // Page<>는 인터페이스이기 때문에 PageImpl<> 구현 클래스로 객체를 생성해야 한다.
        // 데이터의 내용(List<T> 컬렉션), 페이징+정렬 처리, 개수
        // Object[0]: 데이터의 내용, Object[1]: 페이징+정렬 된 데이터 ,Object[2]: 개수
        return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
    }
}
