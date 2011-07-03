package name.samolisov.spring.demo.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import name.samolisov.spring.demo.dao.IDemoDao;
import name.samolisov.spring.demo.entity.Auto;
import name.samolisov.spring.demo.entity.Person;

public class PersonService implements IPersonService {

	private IDemoDao dao;

	public void setDao(IDemoDao dao) {
		this.dao = dao;
	}

	@Override
	public Person getPerson(String name) {
		Person person = dao.getPersonByName(name);
		// some sophisticated transformations for lazy loading demo
		for (Auto auto : person.getAutos())
			System.out.println("Auto: " + auto.getName() + " - " + auto.getYear());

		return person;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveAuto(Auto auto) {
		dao.saveAuto(auto);
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void savePerson(Person person) {
		dao.savePerson(person);
	}
}
