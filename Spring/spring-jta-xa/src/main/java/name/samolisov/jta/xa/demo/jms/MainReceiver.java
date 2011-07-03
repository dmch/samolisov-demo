package name.samolisov.jta.xa.demo.jms;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainReceiver {

	public static void main(String[] args) {
		try {
			// Starting receivers from the Spring's context
	        new ClassPathXmlApplicationContext(new String[]{
	        		"spring-context-jdbc.xml", "spring-context-jms-common.xml", "spring-context-jms-receivers.xml"});
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
