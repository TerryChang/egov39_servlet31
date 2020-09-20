package egovframework.mybatis.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JobResultVO {

    public static final String OK = "OK";
    public static final String FAIL = "FAIL";
    public static final String SUCCESS_MESSAGE = "success";

    private final String result;
    private final String message;

    @Builder
    public JobResultVO(String result, String message) {
        this.result = result;
        this.message = message;
    }

}
