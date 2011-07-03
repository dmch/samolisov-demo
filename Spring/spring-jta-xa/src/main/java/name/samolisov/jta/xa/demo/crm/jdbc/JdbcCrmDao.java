package name.samolisov.jta.xa.demo.crm.jdbc;

import name.samolisov.jta.xa.demo.crm.Account;
import name.samolisov.jta.xa.demo.crm.ICrmDao;
import name.samolisov.jta.xa.demo.crm.Order;

import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class JdbcCrmDao extends SimpleJdbcDaoSupport implements ICrmDao {

	private static final String ACCOUNTS_TABLE = "TBL_ACCOUNT";

	private static final String INSERT_ACCOUNT_QUERY = "insert into " + ACCOUNTS_TABLE +
		" (id, name, product_name, count, price, total) values (account_id_seq.nextval, ?, ?, ?, ?, ?)";

	private static final String ORDER_TABLE = "TBL_ORDER";

	private static final String INSERT_ORDER_QUERY = "insert into " + ORDER_TABLE +
		" (id, name, product_name, price) values (order_id_seq.nextval, ?, ?, ?)";

	@Override
	public void addAccount(Account account) {
		getSimpleJdbcTemplate().update(INSERT_ACCOUNT_QUERY, account.getName(), account.getProductName(),
				account.getCount(), account.getPrice(), account.getTotal());
	}

	@Override
	public void addOrder(Order order) {
		getSimpleJdbcTemplate().update(INSERT_ORDER_QUERY, order.getName(), order.getProductName(), order.getPrice());
	}
}
