package egovframework.mybatis.service;

import java.io.IOException;
import java.util.List;

import egovframework.mybatis.vo.UploadFileVO;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;

/**
 * @param <T> 게시판 클래스
 * @param <S> 게시판 검색시 검색파라미터 객체로 사용되는 클래스
 * @param <C> 게시판 조회갯수로 return 되는 값을 표현하는 클래스
 * @param <K> 게시판 Primary Key로 사용되는 클래스
 */
public interface BoardService<T, S, C, K> {
	
	/**
	 * 게시물 리스트를 조회한다
	 * @param s
	 * @return
	 */
	public List<T> list(S s) throws Exception;

	/**
	 * 게시물 리스트의 전체 갯수를 조회한다
	 * @param s
	 * @return
	 */
    public C count(S s) throws Exception;

    /**
     * 특정 게시물을 조회한다
     * @param k
     * @return
     */
    public T view(K k) throws Exception;

    /**
     * 게시물 여러개를 삭제한다
     * @param k
     */
    public void delete(K[] k) throws Exception;

}
