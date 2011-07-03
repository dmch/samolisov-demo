package name.samolisov.jta.xa.demo.eshop;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.annotation.Resource;

import name.samolisov.jta.xa.demo.eshop.IEshopService;
import name.samolisov.jta.xa.demo.eshop.IProductDao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-context-jdbc.xml"})
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})
public class EshopServiceTest {

	private static final Long TEST_UID = 1L;

	@Resource
	private IEshopService service;

	@Resource
	private IProductDao dao;

	@Test
	public void testInjectEshopService() {
		assertNotNull(service);
	}

	@Test
	public void testGetPrice() {
		double price = service.getPrice(TEST_UID);
		assertTrue(price == 100000);
	}

	@Test
	public void testGetProductName() {
		String productName = service.getProductName(TEST_UID);
		assertNotNull(productName);
		assertTrue("BMW X5".equals(productName));
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	public void testSell1() throws Exception {
		int oldCount = getCount();
		service.sell(TEST_UID, 1);
		int newCount = getCount();
		assertTrue(oldCount - newCount == 1);
	}

	@Test
	@Transactional(propagation = Propagation.REQUIRED)
	public void testSell2() throws Exception {
		int oldCount = getCount();
		try {
			service.sell(TEST_UID, 10);
			fail();
		}
		catch (Exception e) {
			assertTrue(getCount() == oldCount);
		}
	}

	private int getCount() {
		return dao.getCount(TEST_UID);
	}
}
