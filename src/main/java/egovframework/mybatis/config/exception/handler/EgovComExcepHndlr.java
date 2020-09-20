package egovframework.mybatis.config.exception.handler;

import egovframework.rte.fdl.cmmn.exception.handler.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EgovComExcepHndlr implements ExceptionHandler {

	@Override
	public void occur(Exception exception, String packageName) {
		// TODO Auto-generated method stub
		logger.debug("[HANDLER][PACKAGE]::: {}", packageName);
		logger.debug("[HANDLER][Exception]:::", exception);
	}
	

}
