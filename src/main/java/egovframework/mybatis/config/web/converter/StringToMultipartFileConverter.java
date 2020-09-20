package egovframework.mybatis.config.web.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import egovframework.mybatis.vo.UploadFileVO;

public class StringToMultipartFileConverter implements Converter<String, MultipartFile> {

	@Override
	public MultipartFile convert(String source) {
		// TODO Auto-generated method stub
		return null;
	}

}
