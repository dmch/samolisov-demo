package name.samolisov.jta.xa.demo.eshop;

public interface IEshopService {

	public double getPrice(Long productId);

	public String getProductName(Long productId);

	public void sell(Long productId, Integer count) throws Exception;
}
