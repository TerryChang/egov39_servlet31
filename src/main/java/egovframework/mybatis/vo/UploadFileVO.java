package egovframework.mybatis.vo;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;
import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("upload_file")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class UploadFileVO {

	private long idx = 0;
	
	@Setter
	private long boardIdx = 0;
    private String orgFileName = "";
    
    @Setter
    private String realFileName = "";
    
    @Setter
    private String ext;
    private long fileSize = 0;
    
    @EqualsAndHashCode.Exclude
    private LocalDateTime regdate;
    
    @Setter
    private File file;
    private MultipartFile multipartFile;
    
    @Builder
    public UploadFileVO(long idx, String orgFileName, String realFileName, String ext, long fileSize, File file, MultipartFile multipartFile) {
    	this.idx = idx;
        this.orgFileName = orgFileName;
        this.realFileName = realFileName;
        this.ext = ext;
        this.fileSize = fileSize;
        this.file = file;
        this.multipartFile = multipartFile;
    }
    
    public void transferFile() throws IllegalStateException, IOException {
    	multipartFile.transferTo(file);
    }
    
    public void transferFile(String filePath) throws IllegalStateException, IOException {
    	file = new File(filePath);
    	multipartFile.transferTo(file);
    }
    
    /**
     * 업로드 된 파일의 실제 물리적인 경로값이 저장되어 있는 realFileName을 이용해서 업로드된 파일을 삭제한다
     */
    public void deleteFileByRealFileName() {
    	file = new File(realFileName);
    	file.delete();
    }
}
