package name.samolisov.jta.xa.demo.eshop.jdbc;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import name.samolisov.jta.xa.demo.eshop.IProductDao;
import name.samolisov.jta.xa.demo.eshop.Product;

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
public class JdbcProductDaoTest {

	private static final Long TEST_UID = 1L;

	@Resource
	private IProductDao dao;

	@Test
	public void testInjectProductDao() {
		assertNotNull(dao);
	}

	@Test
	public void testGetAllProducts() {
		List<Product> products = dao.getAllProducts();
		assertNotNull(products);
		assertFalse(products.size() == 0);
	}

	@Test
	public void testGetPrice() {
		double price = dao.getPrice(TEST_UID);
		assertFalse(price == 0);
		assertTrue(price == 100000);
	}

	@Test
	public void testGetCount() {
		int count = dao.getCount(TEST_UID);
		assertTrue(count == 5);
	}

	@Test
	public void testGetProductName() {
		String productName = dao.getProductName(TEST_UID);
		assertNotNull(productName);
		assertTrue("BMW X5".equals(productName));
	}

	@Test
	@Transactional
	public void testUpdateCount() {
		dao.updateCount(TEST_UID, 10);
		int count = dao.getCount(TEST_UID);
		assertTrue(count == 10);
	}
}
