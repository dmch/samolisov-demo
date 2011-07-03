package name.samolisov.spring.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        ISomeTestFacade facade = (ISomeTestFacade) ctx.getBean("testFacade");

        facade.init();
        facade.loadPerson();
        facade.fetchAutos();
    }
}
