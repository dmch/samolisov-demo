package name.samolisov.jta.xa.demo.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;

import name.samolisov.jta.xa.demo.eshop.IEshopService;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

public class JmsIntegratorService {

	private JmsTemplate crmJmsTemplate;

	private IEshopService eshopService;

	@Transactional(rollbackFor = Exception.class)
	public void makeTransaction(final Long productId, final String customerName, final int count) throws Exception {
		// Узнать актуальную цену и название товара (ESHOP)
		final double price = eshopService.getPrice(productId);
		final String productName = eshopService.getProductName(productId);

		// Ввести заказы в систему и выставить счет покупателю (CRM)
		crmJmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setStringProperty("name", customerName);
				message.setStringProperty("productName", productName);
				message.setDoubleProperty("price", price);
				message.setIntProperty("count", count);

				return message;
			}
		});

		// Обновить сведения о наличии товара на складе (ESHOP)
		eshopService.sell(productId, count);

		throw new Exception();
	}

	public void setCrmJmsTemplate(JmsTemplate crmJmsTemplate) {
		this.crmJmsTemplate = crmJmsTemplate;
	}

	public void setEshopService(IEshopService eshopService) {
		this.eshopService = eshopService;
	}
}
