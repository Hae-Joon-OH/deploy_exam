package com.nomadlab.boot01.repository;

import com.nomadlab.boot01.domain.Board;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {
    @Autowired
    private BoardRepository boardRepository;

//    @Test
//    public void testInsert() {
//        for(int i = 1; i <= 100; i++) {
//            Board board = Board.builder()
//                    .title("title...")
//                    .content("content..." + i)
//                    .writer("user" + (i % 10))
//                    .build();
//            Board result = boardRepository.save(board);
//            log.info("BNO : " + result.getBno());
//        }
//    }

    @Test
    public void testSelect() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();
        log.info(board);
    }

    @Test
    public void testUpdate() {
        Long bno = 100L;
        Optional<Board> result = boardRepository.findById(bno);
        Board board = result.orElseThrow();

        board.change("update... title 100", "update content 100");
        boardRepository.save(board);
    }

    @Test
    public void testUpdate2() {
        // 이 메서드를 실행하면 에러가 발생
        // 왜냐하면...
        // JPA는 올려놓았던 모든 데이터를 불러와서 수정을 하는데,,
        // writer를 언급하지 않아서 오류가 발생
        // 즉, update하려면 수정하려는 컬럼만 넣으면 되는게 아니라 수정 안할 컬럼들도
        // 전부 언급해줘야 한다는 뜻이다... 요런면에서는 조금 불편...
        Long bno = 100L;
        Board board = Board.builder()
                .bno(bno)
                .title("title...")
                .content("content... update2")
                .build();
        boardRepository.save(board);
    }

    @Test
    public void testUpdate3() {
        // 없는 bno를 지정한 경우
        Long bno = 1000L;
        Board board = Board.builder()
                .bno(bno)
                .title("title...")
                .content("content...update3")
                .writer("user..update")
                .build();
        boardRepository.save(board);
        // select를 먼저 실행해서 데이터 베이스의 데이터 전체를 훑고 해당 bno가 있는지 확인
        // 그 후 있으면 update 실행, 없으면 insert를 실행해서 추가해버린다.
    }

    @Test
    public void testDelete() {
        Long bno = 1L;

        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging() {
        // 1page order by bno desc
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count : " + result.getTotalElements());
        log.info("total page : " + result.getTotalPages());
        log.info("page number : " + result.getNumber());
        log.info("page size : " + result.getSize());
        // prev next
        log.info(result.hasPrevious() + " : " + result.hasNext());

        List<Board> boardList = result.getContent();

        boardList.forEach(board -> log.info(board));

    }

    @Test
    public void testSearch1() {
        // 2 page order by bno desc
        Pageable pageable = PageRequest.of(1, 10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test
    public void testSearchAll() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
    }

    @Test
    public void testSearchAll2() {
        String[] types = {"t", "c", "w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        // total pages
        log.info(result.getTotalPages());

        // page size
        log.info(result.getSize());

        // pageNumber
        log.info(result.getNumber());

        // prev next
        log.info(result.hasPrevious() + " : " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }
}
