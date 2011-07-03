package name.samolisov.jta.xa.demo.eshop;

import java.util.List;

public interface IProductDao {

	public List<Product> getAllProducts();

	public double getPrice(Long productId);

	public String getProductName(Long productId);

	public int getCount(Long productId);

	public void updateCount(Long productId, Integer newCount);
}
