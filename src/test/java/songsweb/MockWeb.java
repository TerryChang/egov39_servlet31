package songsweb;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@WebAppConfiguration
public class MockWeb {
	
	@Autowired
	MockHttpServletResponse mockHttpServletResponse;
	
	@Test
	public void HttpServletResponse에_header_추가하기() {
		mockHttpServletResponse.addHeader("testTerryHeader", "myvalue");
		
		Collection<String> headerNames = mockHttpServletResponse.getHeaderNames();
    	
    	for(String header : headerNames) {
    		System.out.println(header + " : " + mockHttpServletResponse.getHeader(header));
    	}
    	
    	String strErrorMsg = "test";
    	int iErrorCode = 200;
    	String RESPONSE_HEADER_MESSAGE = "{\"code\":\"%d\", \"message\":\"%s\" }";
    	String strResult = String.format(RESPONSE_HEADER_MESSAGE, new Integer(iErrorCode), strErrorMsg);
	}

}