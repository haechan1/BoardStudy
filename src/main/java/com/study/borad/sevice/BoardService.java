package com.study.borad.sevice;

import com.study.borad.entity.Board;
import com.study.borad.repository.BoardRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.List;
import java.util.UUID;

// 이 서비스를 어디서 이용하냐? -> BoardController -> @postMapping으로 넘어 왔을때
@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    // 자바에서 객채 생성할 때(인터페이스라 생성할 수는 없지만) 어노테이션을 사용하면됨 Autowired
    // = new BoardRepository;

    // 글 작성 처리
    public void write(Board board, MultipartFile file) throws Exception { // Board라는 클래스에 board라는 이름의 변수를 받아주기
        // 파일 업로드 처리 시작
        String projectPath = System.getProperty("user.dir") // 프로젝트 경로를 가져옴
                + "\\src\\main\\resources\\static\\files"; // 파일이 저장될 폴더의 경로

        UUID uuid = UUID.randomUUID(); // 랜덤으로 식별자를 생성

        String fileName = uuid + "_" + file.getOriginalFilename(); // UUID와 파일이름을 포함된 파일 이름으로 저장

        File saveFile = new File(projectPath, fileName); // projectPath는 위에서 작성한 경로, name은 전달받을 이름

        file.transferTo(saveFile);

        board.setFilename(fileName); // 디비에 넣기?
        board.setFilepath("/files/" + fileName); // static 아래부분의 파일 경로로만으로도 접근이 가능함
        // 파일 업로드 처리 끝

        boardRepository.save(board); // board를 저장소에 save
    }


    // 게시글 리스트 처리
    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable); //Board라는 class가 담긴 list를 찾아 반환
    }

    // 검색 기능 추가
    public Page<Board> boardSearchList(String searchKeyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(searchKeyword, pageable);
    }

    // 특정 게시글 불러오기
    public Board baordView(Integer id) {
        return boardRepository.findById(id).get();
    }

    // 특정 게시글 삭제
    public void boardDelete(Integer id) {
        // 이미 만들어져있는 메소드를 사용해서 삭제기능을 사용하는거임
        boardRepository.deleteById(id);
    }
}

