package name.samolisov.jta.xa.demo.crm;

public interface ICrmService {

	public void addOrders(String name, String productName, double price, int count) throws Exception;

	public void addAccount(String name, String productName, double price, int count) throws Exception;
}
