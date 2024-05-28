package com.study.borad.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import com.study.borad.entity.Board;
import com.study.borad.sevice.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

// BoardService클래스에서 만든 동작을 여기서 작동시켜서 html페이지로 이동하거나 하는거임.
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService; // boardService.wirte(board) 쓰려면 필요함

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // boardwrite.html이띄어지게
    @GetMapping("/board/write") // localhost:8080/board/write
    public String boardWriteForm() {

        return "boardwrite";
    }

    // boardwrite에서 작성한 애들을 가져오는거임
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file)
            throws Exception { // 데이터가 board에 담겨져서 들어옴

        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    // 데이터를 담아 페이지로 보내기 위해 Model 자료형을 인자로
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) { // 검색 키워드를 searchKeyword로 전달 받음

        Page<Board> list = null;

        if (searchKeyword == null) {  // 검색할 키워드가 들어오지 않은 경우 전체 리스트 출력
            list = boardService.boardList(pageable);
        } else {  // 검색할 키워드가 들어온 경우 검색 기능이 포함된 리스트 반환
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        int nowPage = list.getPageable().getPageNumber() + 1; // pageable이 가지고 있는 페이지는 0부터 시작하기때문에 1을 더함
        int startPage = Math.max(nowPage - 4, 1); // 1보다 작은 경우는 1을 반환
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); // 전체 페이지보다 많은 경우는 전체 페이지를 반환

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardlist";
    }

    // 넘겨주는걸 여기서 하고 출력해주는 부분을 타임리프(boardview.html)에서 처리해줘야함.
    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardview(Model model, Integer id) {

        model.addAttribute("board", boardService.baordView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {

        //BoardService에서 작성한 boardService메소드 사용하기
        boardService.boardDelete(id);
        // 이러면 이제 삭제처리 되는거임
        // Delete라는 주소로 들어왔을때 id값을 찾아내서 넘어온 파라미터(id)값을 boardDelete 메소드에 담아서
        // BoardService에서 작성한 boardService메소드에 보내주면 거기서 삭제처리가 되는거임
        // 그리고 삭제후 어느페이지로 가느냐? redirect를 이용해서 list페이지로 가게할거임
        return "redirect:/board/list";
    }

    // PathVariable이란 {id}이게 인식이 돼서 Integer형태의 ID로 들어오는거임
    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        // 수정버튼 누르면 나오는 화면에 제목 내용이 그대로 따라 나오게(위에 쓴 boardview그대로)
        model.addAttribute("board", boardService.baordView(id));


        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {

        // 1. 먼저 기존에 있던 글을 불러옴
        // Board라는 객체를 생성해서 기존에 있던 글(상세페이지)이 담겨져서 객체 주소에 넣어준다.
        Board boardTemp = boardService.baordView(id);
        // 2. 수정한 데이터board.getTitle을 기존의 boardtemp에 덮어씌우는거임.
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file); // 글 수정 완료

        // 모델에 메시지와 리다이렉션 URL 추가
        model.addAttribute("message", "글 수정이 완료되었습니다."); // 사용자에게 표시할 메시지
        model.addAttribute("searchUrl", "/board/list"); // 완료 후 이동할 URL

        // 메시지와 URL을 포함한 message.html 뷰를 반환
        return "message";
    }



}