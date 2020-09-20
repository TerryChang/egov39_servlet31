package egovframework.mybatis.config.web.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SessionListener implements HttpSessionListener {
	
	private final int sessionTimeoutMinutes;

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		logger.info("Session Created");
		event.getSession().setMaxInactiveInterval(sessionTimeoutMinutes * 60);

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		// TODO Auto-generated method stub

	}

}
