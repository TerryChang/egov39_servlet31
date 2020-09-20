package egovframework.mybatis.config.web.converter;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import egovframework.mybatis.vo.UploadFileVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MultipartFileToUploadFileVOConverter implements Converter<MultipartFile, UploadFileVO> {
	
	private final EgovPropertyService egovPropertyService;

	@Override
	public UploadFileVO convert(MultipartFile source) {
		// TODO Auto-generated method stub
		UploadFileVO result = null;
		
		if(source == null) {
			result = new UploadFileVO(); // 업로드 된게 없을 경우 null을 return 하게 되면 이를 처리하는 쪽에서 null 체크까지 해야 하므로 null로 return 하지 말고 empty 객체 개념으로 return 한다
		} else {	
			String ext = StringUtils.getFilenameExtension(source.getOriginalFilename()); // 확장자
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"); // 연월일시분초밀리초3자리
			String realFileName = egovPropertyService.getString("Globals.fileStorePath") + LocalDateTime.now().format(formatter);
			
			if(ext != null) {
				realFileName += "." + ext;
			}
			
			result = UploadFileVO.builder()
						.orgFileName(source.getOriginalFilename())
						.realFileName(realFileName)
						.ext(ext)
						.fileSize(source.getSize())
						.file(new File(realFileName))
						.multipartFile(source)
						.build();
		}
		return result;
	}

}
