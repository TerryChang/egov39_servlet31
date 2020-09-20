package egovframework.mybatis.mapper;

import java.util.List;

import egovframework.mybatis.vo.UploadFileVO;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper
public interface UploadFileMapper {

	public List<UploadFileVO> listUploadFileByBoardIdxs(Long [] boardIdxs);
	
	// public List<UploadFileVO> listUploadFileByIdxs(Long [] idxs);
	
	public UploadFileVO viewUploadFile(Long idx);
	
	public void insertUploadFile(UploadFileVO uploadFileVO);
	
	// public void deleteUploadFileByIdxs(Long [] idxs);
	
	// public void deleteUploadFileByBoardIdx(Long boardIdx);
	
	void deleteUploadFileByIdx(Long idx);
	
	void deleteUploadFileByBoardIdxs(Long [] boardIdxs);
}
