package name.samolisov.jta.xa.demo.crm.jms;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import name.samolisov.jta.xa.demo.crm.ICrmService;

import org.apache.log4j.Logger;

public class OnCreateAccountAndOrdersListener implements MessageListener {

	private static final Logger _log = Logger.getLogger(OnCreateAccountAndOrdersListener.class);

	private ICrmService service;

	public void setService(ICrmService service) {
		this.service = service;
	}

	@Override
	public void onMessage(Message msg) {
        try {
            MapMessage m = (MapMessage) msg;
            String name = m.getStringProperty("name");
            String productName = m.getStringProperty("productName");
            double price = m.getDoubleProperty("price");
            int count = m.getIntProperty("count");

            _log.debug("Creating an Account with name = " + name + ", product = " + productName
            		+ ", price = " + price + ", count = " + count);

            service.addAccount(name, productName, price, count);

            _log.debug("Creating Orders with name = " + name + ", product = " + productName
            		+ ", price = " + price + ", count = " + count);

            service.addOrders(name, productName, price, count);
        }
        catch (Exception e) {
            _log.error("Could not handle a message", e);

            // выбросим Runtime Exception, чтобы откатить транзакцию
            throw new RuntimeException(e.getMessage());
        }
	}
}
