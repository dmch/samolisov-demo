package name.samolisov.jta.xa.demo.local;

import name.samolisov.jta.xa.demo.crm.ICrmService;
import name.samolisov.jta.xa.demo.eshop.IEshopService;

import org.springframework.transaction.annotation.Transactional;

public class IntegratorService {

	private ICrmService crmService;

	private IEshopService eshopService;

	@Transactional(rollbackFor = Exception.class)
	public void makeTransaction(Long productId, String customerName, int count) throws Exception {
		// Узнать актуальную цену и название товара (ESHOP)
		double price = eshopService.getPrice(productId);
		String productName = eshopService.getProductName(productId);

		// Ввести заказы в систему (CRM)
		crmService.addOrders(customerName, productName, price, count);

		// Выставить счет покупателю (CRM)
		crmService.addAccount(customerName, productName, price, count);

		// Обновить сведения о наличии товара на складе (ESHOP)
		eshopService.sell(productId, count);
	}

	public void setCrmService(ICrmService crmService) {
		this.crmService = crmService;
	}

	public void setEshopService(IEshopService eshopService) {
		this.eshopService = eshopService;
	}
}
