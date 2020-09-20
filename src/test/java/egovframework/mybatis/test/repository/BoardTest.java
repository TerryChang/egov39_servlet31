package egovframework.mybatis.test.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import egovframework.mybatis.mapper.BoardMapper;
import egovframework.mybatis.test.annotation.TestCommon;
import egovframework.mybatis.vo.Board;

@RunWith(SpringRunner.class)
@TestCommon
public class BoardTest {
	
	@Autowired
	BoardMapper boardMapper;
	
	@Test
	public void BoardMapper_Bean_Injection_테스트() {
		assertThat(boardMapper, is(notNullValue()));
	}
	
	@Test
	public void BoardMapper_insertBoard_메소드_테스트() {
		Board board = Board.builder().title("테스트 제목").contents("테스트 컨텐츠").build();
		boardMapper.insertBoard(board);
		Board resultBoard = boardMapper.viewBoard(board.getIdx());
		assertThat(resultBoard, is(notNullValue()));
		assertThat(resultBoard.getTitle(), equalTo(board.getTitle()));
		assertThat(resultBoard.getContents(), equalTo(board.getContents()));
	}
	
	@Test
	public void 존재하지않는_레코드_조회시_null로_return하는지_테스트() {
		Board resultBoard = boardMapper.viewBoard(-100L);
		assertThat(resultBoard, is(nullValue()));
	}
	
}
