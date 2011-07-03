package name.samolisov.spring.demo.service;

import name.samolisov.spring.demo.entity.Auto;
import name.samolisov.spring.demo.entity.Person;

public interface IPersonService {

	public void savePerson(Person person);

	public void saveAuto(Auto auto);

	public Person getPerson(String name);
}
