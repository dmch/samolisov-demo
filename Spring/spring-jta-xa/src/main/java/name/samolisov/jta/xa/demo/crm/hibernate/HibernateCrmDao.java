package name.samolisov.jta.xa.demo.crm.hibernate;

import name.samolisov.jta.xa.demo.crm.Account;
import name.samolisov.jta.xa.demo.crm.ICrmDao;
import name.samolisov.jta.xa.demo.crm.Order;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateCrmDao extends HibernateDaoSupport implements ICrmDao {

	@Override
	public void addAccount(Account account) {
		getHibernateTemplate().save(account);
	}

	@Override
	public void addOrder(Order order) {
		getHibernateTemplate().save(order);
	}
}
