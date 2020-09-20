package egovframework.mybatis.vo;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("board")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Board {
    private long idx;
    private String title;
    private String contents;

    @EqualsAndHashCode.Exclude
    private LocalDateTime regdate;
    
    @EqualsAndHashCode.Exclude
    private List<UploadFileVO> listUploadFileVO;

    @Builder
    public Board(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    @Builder
    public Board(long idx, String title, String contents, LocalDateTime regdate) {
        this.idx = idx;
        this.title = title;
        this.contents = contents;
        this.regdate = regdate;
    }

    @Builder
    public Board(long idx, String title, String contents) {
        this.idx = idx;
        this.title = title;
        this.contents = contents;
    }

}
