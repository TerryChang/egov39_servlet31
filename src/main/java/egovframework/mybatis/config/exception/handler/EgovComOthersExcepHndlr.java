package egovframework.mybatis.config.exception.handler;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EgovComOthersExcepHndlr implements ExceptionHandler {

	@Override
	public void occur(Exception exception, String packageName) {
		// TODO Auto-generated method stub
		logger.error(packageName, exception);
	}
}
