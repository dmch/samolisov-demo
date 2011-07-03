package name.samolisov.jta.xa.demo.crm;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class CrmService implements ICrmService {

	private static final Logger _log = Logger.getLogger(CrmService.class);

	private ICrmDao dao;

	public void setDao(ICrmDao dao) {
		this.dao = dao;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addOrders(String name, String productName, double price, int count) throws Exception {
		for (int i = 0; i < count; i++) {
			addOrder(name, productName, price);
		}
	}

	private void addOrder(String name, String productName, double price) throws Exception {
		Order order = new Order();
		order.setName(name);
		order.setProductName(productName);
		order.setPrice(price);

		_log.debug("Adding order with name = " + order.getName() + "; product = " + order.getProductName()
				+ "; price = " + order.getPrice());

		dao.addOrder(order);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addAccount(String name, String productName, double price, int count) throws Exception {
		Account account = new Account();
		account.setName(name);
		account.setProductName(productName);
		account.setPrice(price);
		account.setCount(count);
		account.setTotal(count * price);

		_log.debug("Adding account with name = " + account.getName() + "; product = " + account.getProductName()
				+ "; price = " + account.getPrice() + "; count = " + account.getCount()
				+ "; total = " + account.getTotal());

		dao.addAccount(account);
	}
}
