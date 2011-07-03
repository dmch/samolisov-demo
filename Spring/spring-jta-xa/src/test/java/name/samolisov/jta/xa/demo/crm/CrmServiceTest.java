package name.samolisov.jta.xa.demo.crm;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import name.samolisov.jta.xa.demo.crm.ICrmService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-context-jdbc.xml"})
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})
public class CrmServiceTest {

	@Resource
	private ICrmService service;

	@Test
	public void testInjectService() {
		assertNotNull(service);
	}

	@Test
	@Transactional
	public void testAddOrders() {
		try {
			service.addOrders("Pavel Samolisov", "BMW X5", 100000, 10);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	@Transactional
	public void testAddAccount() {
		try {
			service.addAccount("Pavel Samolisov", "BMW X5", 100000, 10);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
