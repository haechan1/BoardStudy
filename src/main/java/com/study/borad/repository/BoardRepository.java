// 인터페이스 -> 상속을 받는거?
package com.study.borad.repository;


import com.study.borad.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Jpa를 이용해서 게시글 작성해서 -> 디비에 넣기
@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    // 엔티티는 Board, PK의 타입은 Integer

    // 제목에 포함된 키워드를 찾는 메서드
    // 리턴타입이 Page이고 제네릭<>을 써서 Board라는 객체를 담겠다.
    // 레퍼지토리에 메소드를 추가했기 때문에 서비스에도 메소드를 추가해준다.
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}
