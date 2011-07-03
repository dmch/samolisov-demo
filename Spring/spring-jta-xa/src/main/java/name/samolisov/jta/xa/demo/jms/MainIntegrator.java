package name.samolisov.jta.xa.demo.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainIntegrator {
	public static void main(String[] args) {
		try {
	        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
	        		new String[]{"spring-context-jdbc.xml", "spring-context-jms-common.xml",
	        				"spring-context-jms-senders.xml"});
	        JmsIntegratorService integratorService = (JmsIntegratorService) ctx.getBean("jmsIntegratorService");

	        integratorService.makeTransaction(1L, "Pavel Samolisov", 4);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
