import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;

import egovframework.mybatis.config.web.listener.SessionListener;
import egovframework.rte.ptl.mvc.filter.HTMLTagFilter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MybatisWebApplicationInitializer extends AbstractDispatcherServletInitializer {
	private ApplicationContext rootContext;
	private ApplicationContext servletContext;
	
	@Value("${Session.Timeout}")
	private String sessionTimeout;

	@Override
	protected WebApplicationContext createServletApplicationContext() {
		// TODO Auto-generated method stub
		logger.info("createServletApplicationContext");
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocations("WEB-INF/config/egovframework/springmvc/mybatis-servlet.xml");
		context.setParent(rootContext);
		this.servletContext = context;
		return context;
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		String [] result = {"*.do"};
		return result;
	}

	@Override
	protected WebApplicationContext createRootApplicationContext() {
		// TODO Auto-generated method stub
		logger.info("createRootApplicationContext");
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocations("classpath:profiles/**/*.xml");
		this.rootContext = context;
		return context;
	}

	/**
	 * getServletFilters 메소드를 통해 등록되는 Filter들의 Mapping Url은 Dispatcher Servlet의 Mapping Url을 따라간다
	 * 즉 getServletMappings() 메소드를 통해 등록된 Mapping Url을 그대로 쓴다고 보면 된다
	 */
	@Override
	protected Filter[] getServletFilters() {
		// TODO Auto-generated method stub
		// 상위 클래스인 AbstractDispatcherServletInitializer의 getServletFilters()가 null을 return 하고 있기 때문에 
		// super.getServletFilters() 메소드를 호출하여 상위 클래스가 return 하는 filter를 받아 이를 포함해서 return 할 필요가 없다
		
		List<Filter> filterList = new ArrayList<Filter>();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter("UTF-8");		
		filterList.add(characterEncodingFilter);
		HTMLTagFilter htmlTagFilter = new HTMLTagFilter();
		filterList.add(htmlTagFilter);
		
		return filterList.toArray(new Filter[filterList.size()]);
	}

	// Spring의 Dispatcher Servlet이 아닌 별도의 Servlet을 등록하려 할 경우 이 메소드에서 한다(https://stackoverflow.com/questions/20915528/how-can-i-register-a-secondary-servlet-with-spring-boot 의 Pramod Yadav 답변글 참고)
	// Spring의 Dispatcher Servlet이 아닌 별도의 Servlet이 이용할 Filter를 등록하려 할 경우 이 메소드에서 한다(https://stackoverflow.com/questions/25209319/how-to-do-filter-mapping-in-abstractannotationconfigdispatcherservletinitializer 의 dectarin 답변글 참고)
	// 세션 타임아웃 설정과 관련해서는 https://programmersought.com/article/84591453855/ 참고	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		super.onStartup(servletContext);
		// servletContext.addListener(new SessionListener(Integer.parseInt(sessionTimeout, 10)));
		servletContext.addListener(new SessionListener(20));
	}

}
