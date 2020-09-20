package egovframework.mybatis.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.mybatis.mapper.BoardMapper;
import egovframework.mybatis.mapper.UploadFileMapper;
import egovframework.mybatis.service.BoardServiceWithFile;
import egovframework.mybatis.vo.Board;
import egovframework.mybatis.vo.SearchVO;
import egovframework.mybatis.vo.UploadFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Terry Chang
 *
 * 예외와 관련해서는 EgovAbstractServiceImpl 클래스의 processException 메소드를 이용해서 EgovBizException으로 변환해서 던질수 있다
 * (관련 구현 방법은 view, delete 메소드 참고)
 * 개인적인 의견으로는 EgovBizException으로 던지는 예외는 업무적 단계에서 절차상 필요한 값들의 유무를 판별해서 예외를 던지는 컨셉으로 가는게 좋을것 같다..
 * 현재 예외 샘플은 메소드 파라미터의 null 체크를 가지고 했지만 모든 메소드의 파라미터에 null 체크를 해서 null 일 경우 EgovBizException으로 던지는 것은 먼가 비효율적인 느낌이 있다
 * message key를 이용해서 업무적으로 확실한 메시지를 던질수 있는 그런 예외에만 EgovBizException으로 처리하는것이 어떨까 싶다
 * 
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl extends EgovAbstractServiceImpl implements BoardServiceWithFile<Board, SearchVO, Integer, Long, UploadFileVO, Long> {

	private final BoardMapper boardMapper;
	private final UploadFileMapper uploadFileMapper;
	
	@Transactional(readOnly=true)
	@Override
	public List<Board> list(SearchVO searchVO) throws DataAccessException {
		// TODO Auto-generated method stub
		return boardMapper.listBoard(searchVO);
	}

	@Transactional(readOnly=true)
	@Override
	public Integer count(SearchVO searchVO) throws DataAccessException {
		// TODO Auto-generated method stub
		return boardMapper.countBoard(searchVO);
	}

	@Transactional(readOnly=true)
	@Override
	public Board view(Long idx) throws Exception {
		// TODO Auto-generated method stub
		if(idx == null) {
			String [] messageParam = {"BoardServiceImpl", "view", "idx"};
			throw processException("param.null", messageParam);
		}
		Board board = boardMapper.viewBoard(idx);
		return board;
	}

	@Override
	public void insert(Board board, UploadFileVO[] uploadFileVOs) throws DataAccessException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		boardMapper.insertBoard(board);
		// 업로드된 파일에 대한 작업 시작
		if(uploadFileVOs != null) {
			processUploadFile(board.getIdx(), uploadFileVOs);
		}
	}

	@Override
	public void update(Board board, UploadFileVO[] uploadFileVOs) throws DataAccessException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		boardMapper.updateBoard(board);
		// 업로드된 파일에 대한 작업 시작
		if(uploadFileVOs != null) {
			processUploadFile(board.getIdx(), uploadFileVOs);
		}
	}

	@Override
	public void delete(Long[] idxs) throws Exception {
		if(idxs == null || idxs.length == 0) {
			throw processException("board.delete.noarg");
		}
		// 파일 정보 삭제, 게시물 삭제, 물리 파일 삭제순으로 한다. 이 순으로 해야 파일 삭제 과정에서 예외가 발생할 경우 rollback 하기가 쉽다
		// 먼저 파일 정보들을 조회한다
		List<UploadFileVO> listUploadFileVO = uploadFileMapper.listUploadFileByBoardIdxs(idxs);
		
		// 파일 정보 삭제
		uploadFileMapper.deleteUploadFileByBoardIdxs(idxs);
		
		// 게시물 삭제
		boardMapper.deleteBoard(idxs);
		
		// 물리 파일 삭제
		for(UploadFileVO uploadFileVO : listUploadFileVO) {
			uploadFileVO.deleteFileByRealFileName();
		}
		
	}
	
	@Override
	public void deleteFile(Long idx) throws Exception {
		// TODO Auto-generated method stub
		if(idx == null) {
			String [] messageParam = {"BoardServiceImpl", "deleteFile", "idx"};
			throw processException("param.null", messageParam);
		}
		
		// 삭제하고자 하는 파일 정보 조회
		UploadFileVO uploadFileVO = uploadFileMapper.viewUploadFile(idx);
		
		// 삭제하고자 하는 파일 DB에서 삭제
		uploadFileMapper.deleteUploadFileByIdx(idx);
		
		// 삭제하고자 하는 파일 물리적으로 삭제
		File file = new File(uploadFileVO.getRealFileName());
		file.delete();
	}
	
	/**
	 * 게시물을 등록하거나 수정할때 업로드된 파일을 처리하는 메소드
	 * @param idx	게시물 idx
	 * @param uploadFileVOs	업로드된 파일들의 배열
	 * @throws Exception
	 */
	private void processUploadFile(long idx, UploadFileVO[] uploadFileVOs) throws IllegalStateException, IOException {
		// 업로드된 파일을 복사하는 과정에서 예외가 발생할 수 있다. 
		// 그래서 작업을 어디까지 진행했는지를 체크해두어 예외가 발생할 경우 정상적으로 진행된 파일들을 삭제하도록 한뒤 Spring에서 예외를 처리하도록 하기 위해 예외를 던지도록 한다
		// 예외가 발생할 경우 작업중지하고 예외를 던져야 하기 때문에 반복작업에서 foreach를 사용하는 람다식을 사용할 수 없다
		// 불가능은 아니지만 그럴 경우 wrapper를 만들어서 구현해야 하는 관계로 강제로 람다식을 반드시 써야한다는 룰이 없는 한에는 굳이 억지로 쓸 필요는 없어보인다
		int processFileIdx = -1; // 이 변수를 통해서 어디까지 진행했는지를 기록하도록 한다
		try {
			for(UploadFileVO uploadFileVO : uploadFileVOs) {
				if("".equals(uploadFileVO.getOrgFileName())) { // 업로드시 파일 태그 안에 파일 설정을 하지 않은 상태로 올렸을 경우
					continue;
				}
				uploadFileVO.setBoardIdx(idx);
				uploadFileMapper.insertUploadFile(uploadFileVO);
				uploadFileVO.transferFile();
				processFileIdx++;
			}
		} catch(IllegalStateException | IOException e) {
			for(int i = 0; i < processFileIdx; i++) {
				uploadFileVOs[i].deleteFileByRealFileName();
			}
			throw e;
		}
	}

	
}
