package egovframework.mybatis.test.common;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import egovframework.mybatis.test.annotation.TestProduction;
import egovframework.rte.fdl.property.EgovPropertyService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@TestProduction
public class PropertyReadByProfile {
	
	@Value("${Globals.Url}")
	private String globalsUrl;
	
	@Autowired
	private PropertyPlaceholderConfigurer propertyPlaceholderConfigurer;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private EgovPropertyService egovPropertyService;
	
	
	@Test
	public void printGlobalsUrl() {
		logger.info("Globals.Url : {}", globalsUrl);
	}
	
	@Test
	public void printCurrentProfiles() {
		String [] activeProfiles = environment.getActiveProfiles();
		Arrays.stream(activeProfiles).forEach(item -> logger.info("activeProfile : {}", item));
	}
	
	@Test
	public void printPropertiesServiceByEgovPropertyServiceImpl() {
		// 이걸로는 key 값들을 읽어오지 못했음
		Iterator keys = egovPropertyService.getKeys();
		while(keys.hasNext()) {
			String propertyKey = (String)keys.next();
			logger.info("egovPropertyService.getString({}) : {} ", propertyKey, egovPropertyService.getString(propertyKey));
		}
    	// 수동으로 일일이 지정해서는 해당 key에 대한 값을 읽어왔음
		/*
		logger.info("egovPropertyService.getString(\"pageUnit\") : {} ", egovPropertyService.getString("pageUnit"));
		logger.info("egovPropertyService.getString(\"pageSize\") : {} ", egovPropertyService.getString("pageSize"));
		logger.info("egovPropertyService.getString(\"posblAtchFileSize\") : {} ", egovPropertyService.getString("posblAtchFileSize"));
		logger.info("egovPropertyService.getString(\"Globals.fileStorePath\") : {} ", egovPropertyService.getString("Globals.fileStorePath"));
		logger.info("egovPropertyService.getString(\"Globals.addedOptions\") : {} ", egovPropertyService.getString("Globals.addedOptions"));
		*/
	}

}
