package egovframework.mybatis.vo;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Alias("searchVO")
@Getter
@Setter
public class SearchVO {
    private String searchType;
    private String searchWord;
    private int pageNo;
    private int pageSize;

    public SearchVO() {
        this.pageNo = 1;
        this.pageSize = 10;
    }

    @Builder
    public SearchVO(String searchType, String searchWord) {
        this.searchType = searchType;
        this.searchWord = searchWord;
        this.pageNo = 1;
        this.pageSize = 10;
    }

    @Builder
    public SearchVO(String searchType, String searchWord, int pageNo, int pageSize) {
        this(searchType, searchWord);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}
