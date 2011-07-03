package name.samolisov.jta.xa.demo.local;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		try {
	        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-context-jdbc.xml");
			IntegratorService service = (IntegratorService) ctx.getBean("integratorService");

			service.makeTransaction(1L, "Pavel Samolisov", 4);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
