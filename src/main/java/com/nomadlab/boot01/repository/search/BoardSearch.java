package com.nomadlab.boot01.repository.search;

import com.nomadlab.boot01.domain.Board;
import com.nomadlab.boot01.dto.BoardDTO;
import com.nomadlab.boot01.dto.PageRequestDTO;
import com.nomadlab.boot01.dto.PageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {
    Page<Board> search1(Pageable pageable);
    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);

}
