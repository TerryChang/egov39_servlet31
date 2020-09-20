package egovframework.mybatis.test.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import egovframework.mybatis.mapper.BoardMapper;
import egovframework.mybatis.test.annotation.TestCommonWeb;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
// @ContextConfiguration({"classpath:profiles/**/*.xml", "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/mybatis-servlet.xml"})
// @ContextConfiguration({"classpath:profiles/**/*.xml", "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/mybatis-servlet.xml"})
@TestCommonWeb
public class CheckBeanTest {

	@Autowired
	private ConversionService conversionService;

	
	@Test
	public void ConversionService_Bean_Injection_테스트() {
		assertThat(conversionService, is(notNullValue()));
	}
	
}
