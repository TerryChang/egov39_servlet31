package egovframework.mybatis.config.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import egovframework.mybatis.vo.UploadFileVO;

public class StringToUploadFileVOConverter implements Converter<String, UploadFileVO> {

	@Override
	public UploadFileVO convert(String source) {
		// TODO Auto-generated method stub
		return new UploadFileVO();
	}

}
