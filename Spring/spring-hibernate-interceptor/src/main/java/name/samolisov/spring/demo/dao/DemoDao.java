package name.samolisov.spring.demo.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import name.samolisov.spring.demo.entity.Auto;
import name.samolisov.spring.demo.entity.Person;

public class DemoDao extends HibernateDaoSupport implements IDemoDao {

	@Override
	public Person getPersonByName(final String name) {
		return (Person) getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException,
					SQLException {
				return session.getNamedQuery("getPersonByName")
							  .setParameter("name", name)
							  .setMaxResults(1)
							  .uniqueResult();
			}
		});
	}

	@Override
	public void savePerson(Person person) {
		getHibernateTemplate().save(person);
	}

	@Override
	public void saveAuto(Auto auto) {
		getHibernateTemplate().save(auto);
	}
}
