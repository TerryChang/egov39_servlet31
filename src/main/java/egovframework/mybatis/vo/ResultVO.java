package egovframework.mybatis.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * @param <T>
 */
@Getter
public class ResultVO<T> {
    List<T> boardResults;
    long totalElements;
    int pageNo;
    int pageSize;
    int pageCnt;

    @Builder
    public ResultVO(List<T> boardResults, long totalElements, SearchVO searchVO) {
        this.boardResults = boardResults;
        this.totalElements = totalElements;
        this.pageNo = searchVO.getPageNo();
        this.pageSize = searchVO.getPageSize();
        pageCnt = (int) (totalElements / pageSize) + (totalElements % pageSize == 0 ? 0 : 1);
    }
}
