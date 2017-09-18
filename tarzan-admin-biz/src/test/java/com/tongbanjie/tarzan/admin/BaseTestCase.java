package com.tongbanjie.tarzan.admin;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 单元测试基类<p/>
 * <其他单元测试都继承本类>
 *
 * @author zixiao
 * @date 17/1/18
 */
@ContextConfiguration(locations="classpath:/META-INF/spring/admin-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseTestCase {

}
