package name.samolisov.spring.demo.dao;

import name.samolisov.spring.demo.entity.Auto;
import name.samolisov.spring.demo.entity.Person;

public interface IDemoDao {

	public Person getPersonByName(String name);

	public void savePerson(Person person);

	public void saveAuto(Auto auto);
}
