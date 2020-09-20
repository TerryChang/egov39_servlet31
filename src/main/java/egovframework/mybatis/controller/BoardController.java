package egovframework.mybatis.controller;

import java.util.List;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.mybatis.service.BoardServiceWithFile;
import egovframework.mybatis.vo.Board;
import egovframework.mybatis.vo.JobResultVO;
import egovframework.mybatis.vo.SearchVO;
import egovframework.mybatis.vo.UploadFileVO;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardServiceWithFile<Board, SearchVO, Integer, Long, UploadFileVO, Long> boardService;

    @GetMapping("/list")
    public String list(@ModelAttribute(name = "searchVO") SearchVO searchVO, Model model, HttpServletResponse httpServletResponse) throws Exception {
    	
    	List<Board> resultList = boardService.list(searchVO);
    	int totCnt = boardService.count(searchVO).intValue();
    	
    	PaginationInfo paginationInfo = new PaginationInfo();
    	paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
    	paginationInfo.setCurrentPageNo(searchVO.getPageNo());
    	paginationInfo.setTotalRecordCount(totCnt);
    	paginationInfo.setPageSize(10);				// 페이지 목록에서 몇개의 페이지를 보여줘야 하는지를 설정
    	
    	model.addAttribute("resultList", resultList);
		model.addAttribute("resultCnt", totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
    	
        return "/list";
    }

    @GetMapping(value = "/insert")
    public String insertGet(@ModelAttribute(name = "board") Board board) {
        return "/insert";
    }

    @PostMapping(value = "/insert")
    @ResponseBody
    public JobResultVO insertPost(@ModelAttribute(name = "board") Board board, @RequestParam(name="uploadFile", required=false) UploadFileVO [] uploadFileVO, Model model) throws Exception {
    	logger.info("insert method");
        boardService.insert(board, uploadFileVO);
        return JobResultVO.builder().result(JobResultVO.OK).build();
    }

    @GetMapping(value = "/view")
    public String view(@RequestParam(value = "idx") long idx, SearchVO searchVO, Model model) throws Exception {
        Board board = boardService.view(idx);
        model.addAttribute("board", board);
        model.addAttribute("searchVO", searchVO);
        return "/view";
    }

    @GetMapping(value = "/update")
    public String updateGet(@RequestParam(value = "idx") long idx, SearchVO searchVO, Model model) throws Exception {
        Board board = boardService.view(idx);
        model.addAttribute("board", board);
        model.addAttribute("searchVO", searchVO);
        return "/update";
    }

    /*
    @PostMapping(value = "/update")
    public String updatePost(@ModelAttribute(name = "board") Board board, @RequestParam(name="uploadFileNo", required=false) UploadFileVO [] uploadFileVO, SearchVO searchVO, Model model)  throws Exception{
        boardService.update(board, uploadFileVO);
        StringBuffer sbUrl = new StringBuffer();
        sbUrl.append("/board/view.do")
        	.append("?idx=" + board.getIdx())
        	.append("&pageNo=" + searchVO.getPageNo())
        	.append("&pageSize=" + searchVO.getPageSize())
        	.append("&searchType=" + searchVO.getSearchType())
        	.append("&searchWord=" + searchVO.getSearchWord());
        return "redirect:" + sbUrl.toString();
    }
    */

    @PostMapping(value = "/update")
    @ResponseBody
    public JobResultVO updatePost(@ModelAttribute(name = "board") Board board, @RequestParam(name="uploadFile", required=false) UploadFileVO [] uploadFileVO, Model model) throws Exception {
    	logger.info("update method");
        boardService.update(board, uploadFileVO);
        return JobResultVO.builder().result(JobResultVO.OK).build();
    }

    /**
     * Ajax 방식으로 삭제할때는 이 메소드를 사용
     * @param delchk
     * @param httpServletResponse
     * @return
     */
    /*
    @ResponseBody
    @PostMapping(value = "/delete")
    public JobResultVO deletePostAjax(@RequestParam(name = "delchk[]") Long[] delchk, HttpServletResponse httpServletResponse) {
        JobResultVO result = null;
        try {
            boardService.delete(delchk);
            result = JobResultVO.builder().result(JobResultVO.OK).build();
        } catch (Exception e) {
            result = JobResultVO.builder().result(JobResultVO.FAIL).message(e.getMessage()).build();
            httpServletResponse.setStatus(500);
        }

        return result;
    }
    */
    
    
    /**
     * ajax 방식이 아니라 form submit 방식을 사용할때는 @RequestParam(name = "delchk") 이런 식으로 배열형태로 표현하지 말고 그냥 checkbox name 값만 사용한다
     * @param delchk
     * @param httpServletResponse
     * @return
     */
    @PostMapping(value = "/delete")
    public String deletePostForm(@RequestParam(name = "delchk") Long[] delchk, HttpServletResponse httpServletResponse) throws Exception {
    	logger.info("deletePost method start");
        boardService.delete(delchk);
        return "redirect:/board/list.do";
    }
    
    @PostMapping(value="/deleteFile")
    @ResponseBody
    public JobResultVO deleteFile(@RequestParam(name="idx") long idx) throws Exception {
    	logger.info("deleteFile method");
        boardService.deleteFile(idx);
        return JobResultVO.builder().result(JobResultVO.OK).build();
    }
    
    
    /**
     * 테스트 용도로 만들어진 메소드로 게시판 테이블의 레코드를 먼저 삭제한 뒤의 이 URL을 이용해서 테스트 데이터를 집어넣은후 그 넣은 결과를 대상으로 리스트 화면을 만든다
     * 파라미터는 count를 사용하며 만약 사용하지 않을 경우 default로 50이 설정되어 50개가 들어가게 된다
     * @param searchVO
     * @param count
     * @param model
     * @return
     */
    @GetMapping("/testlist")
    public String testlist(@ModelAttribute(name = "searchVO") SearchVO searchVO, @RequestParam(value="count", defaultValue="50") int count, Model model) throws Exception {
    	
    	// parameter로 넘겨진 등록갯수대로 데이터를 등록한뒤 조회결과를 보여준다
    	Board board = new Board();
    	IntStream.rangeClosed(1, count).forEach(i -> {
    		board.setTitle("테스트 제목 " + String.valueOf(i));
    		board.setContents("테스트 내용 " + String.valueOf(i));
    		try {
				boardService.insert(board, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    	
    	List<Board> resultList = boardService.list(searchVO);
    	int totCnt = boardService.count(searchVO).intValue();
    	
    	PaginationInfo paginationInfo = new PaginationInfo();
    	paginationInfo.setRecordCountPerPage(searchVO.getPageSize());
    	paginationInfo.setCurrentPageNo(searchVO.getPageNo());
    	paginationInfo.setTotalRecordCount(totCnt);
    	paginationInfo.setPageSize(10);				// 페이지 목록에서 몇개의 페이지를 보여줘야 하는지를 설정
    	
    	model.addAttribute("resultList", resultList);
		model.addAttribute("resultCnt", totCnt);
		model.addAttribute("paginationInfo", paginationInfo);
    	
        return "/list";
    }
}
