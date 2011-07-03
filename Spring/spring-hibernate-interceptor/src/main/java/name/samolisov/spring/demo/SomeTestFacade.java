package name.samolisov.spring.demo;

import name.samolisov.spring.demo.entity.Auto;
import name.samolisov.spring.demo.entity.Person;
import name.samolisov.spring.demo.service.IPersonService;

public class SomeTestFacade implements ISomeTestFacade {

	private IPersonService service;

	private Person person;

	@Override
	public void init() {
		Person person = new Person();
        person.setName("Pavel");

        Auto auto = new Auto();
        auto.setName("Lada Kalina");
        auto.setYear(2005);

        person.addAuto(auto);

        service.savePerson(person);
	}

	@Override
	public void loadPerson() {
		person = service.getPerson("Pavel");
		System.out.println("id = " + person.getId());
		System.out.println("name = " + person.getName());
	}

	@Override
	public void fetchAutos() {
		for (Auto auto : person.getAutos()) {
        	System.out.println(auto.getName() + " - " + auto.getYear());
        }
	}

	public void setService(IPersonService service) {
		this.service = service;
	}
}
