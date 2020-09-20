package egovframework.mybatis.service;

import java.util.List;

import egovframework.mybatis.vo.UploadFileVO;

/**
 * 
 * @author Terry Chang
 *
 * @param <T>
 * @param <S>
 * @param <C>
 * @param <K>
 * @param <F> 다뤄지는 파일 클래스 타입
 * @param <FK> 파일 관련 Key 데이터 타입
 */
public interface BoardServiceWithFile<T, S, C, K, F, FK> extends BoardService<T, S, C, K> {	
	
	/**
	 * 게시물과 업로드 된 파일 객체들을 이용해서 게시물을 등록한다
	 * @param t
	 * @param f
	 * @throws Exception
	 */
    void insert(T t, F[] f) throws Exception;

    /**
     * 게시물과 업로도된 파일 객체들을 이용해서 게시물을 수정한다
     * @param t
     */
    void update(T t, F[] f) throws Exception;
    
    /**
     * 게시물에 첨부된 파일을 삭제한다
     * @param fk			첨부파일 key
     * @throws Exception
     */
    void deleteFile(FK fk) throws Exception;

}
