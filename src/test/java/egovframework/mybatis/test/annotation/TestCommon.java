package egovframework.mybatis.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
// @ContextConfiguration(locations = {"classpath:profiles/common/spring/*.xml"})
@ContextConfiguration(locations = {"classpath:profiles/**/*.xml"})
@Transactional
public @interface TestCommon {

}
