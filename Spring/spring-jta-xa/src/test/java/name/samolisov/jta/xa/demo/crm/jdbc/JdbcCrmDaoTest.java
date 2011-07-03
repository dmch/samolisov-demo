package name.samolisov.jta.xa.demo.crm.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import name.samolisov.jta.xa.demo.crm.Account;
import name.samolisov.jta.xa.demo.crm.ICrmDao;
import name.samolisov.jta.xa.demo.crm.Order;

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
public class JdbcCrmDaoTest {

	@Resource
	private ICrmDao dao;

	@Test
	public void testInjectCrmDao() {
		assertNotNull(dao);
	}

	@Test
	@Transactional
	public void testAddAccount() {
		Account testAccount = new Account();
		testAccount.setName("Pavel Samolisov");
		testAccount.setProductName("BMW X5");
		testAccount.setCount(5);
		testAccount.setPrice(100000.0);
		testAccount.setTotal(100000.0 * 5);

		try {
			dao.addAccount(testAccount);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	@Transactional
	public void testAddOrder() {
		Order testOrder = new Order();
		testOrder.setName("Pavel Samolisov");
		testOrder.setProductName("BMW X5");
		testOrder.setPrice(100000.0);

		try {
			dao.addOrder(testOrder);
		}
		catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
