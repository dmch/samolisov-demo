package name.samolisov.jta.xa.demo.local.hibernate;

import name.samolisov.jta.xa.demo.local.IntegratorService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		try {
	        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
	        		new String[]{"spring-context-jdbc.xml", "spring-context-hibernate.xml"});
			IntegratorService service = (IntegratorService) ctx.getBean("hibernateIntegratorService");

			service.makeTransaction(1L, "Pavel Samolisov", 4);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
