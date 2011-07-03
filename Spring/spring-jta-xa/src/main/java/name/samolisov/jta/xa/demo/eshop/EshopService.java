package name.samolisov.jta.xa.demo.eshop;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class EshopService implements IEshopService {

    private static final Logger _log = Logger.getLogger(EshopService.class);

	private IProductDao dao;

	public void setDao(IProductDao dao) {
		this.dao = dao;
	}

	@Override
	public double getPrice(Long productId) {
		double price = dao.getPrice(productId);
		_log.debug("Price for id " + productId + " is " +  price);
		return price;
	}

	@Override
	public String getProductName(Long productId) {
		String name = dao.getProductName(productId);
		_log.debug("Product name for id " + productId + " is " + name);
		return name;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void sell(Long productId, Integer count) throws Exception {
		int hasCount = dao.getCount(productId);
		if (hasCount < count)
			throw new Exception("Could not sell " + count + ". We have only " + hasCount);
		dao.updateCount(productId, hasCount - count);
	}
}
