package egovframework.mybatis.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.mybatis.vo.Board;
import egovframework.mybatis.vo.SearchVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface BoardMapper {
    public List<Board> listBoard(SearchVO searchVO);

    public int countBoard(SearchVO searchVO);

    public Board viewBoard(Long idx);

    // mybatis 3.5 버전부터 조회 결과가 없을 경우 null이 아닌 Optional 객체를 return 하도록 지원하고 있으나
    // 전자정부프레임워크 3.9 버전에서 사용중인 mybatis가 3.4.6 인 관계로 이 메소드는 주석처리했다
    // mybatis 버전을 올릴까 했지만 그럴경우 전자정부프레임워크쪽에서 제공하는 클래스들에 문제가 생길 소지가 있을듯 하여 버전업하지 않았다
    // public Optional<Board> viewBoardOptional(long idx);

    public void insertBoard(Board board);

    public void updateBoard(Board board);

    public void deleteBoard(Long[] idxs);
}
