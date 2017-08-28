package com.github.jeannyil.fis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PrepareHttpResponseProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		int messageCount = exchange.getProperty("messageToSend", Integer.class);
		
		int transactionBatchSize = new Integer(exchange.getContext().resolvePropertyPlaceholders("{{default.transaction.batch.size}}"));
		if (exchange.getProperty("requestTransactionBatchSize") != null)
			transactionBatchSize = exchange.getProperty("requestTransactionBatchSize", Integer.class);
		
		boolean transacted = new Boolean(exchange.getContext().resolvePropertyPlaceholders("{{default.transacted}}"));
		if (exchange.getProperty("transacted") != null)
			transacted = exchange.getProperty("transacted", Boolean.class);
		
		int messageSize = new Integer(exchange.getContext().resolvePropertyPlaceholders("{{default.message.size}}"));
		
		long msgTTL = new Long(exchange.getContext().resolvePropertyPlaceholders("{{default.message.ttl}}"));
		if (exchange.getProperty("msgTTL") != null) {
			msgTTL = exchange.getProperty("msgTTL", Long.class);
		}
		
		String deliveryMode = "PERSISTENT";
		if (exchange.getProperty("persistent") != null) {
			if (exchange.getProperty("persistent",String.class).equalsIgnoreCase("false"))
				deliveryMode = "NON_PERSISTENT";
		}
		
		String jmsxGroupID = null;
		if (exchange.getProperty("messageSize") != null) {
			messageSize = exchange.getProperty("messageSize", Integer.class);
		}
		if (exchange.getProperty("JMSXGroupID") != null) {
			jmsxGroupID = exchange.getProperty("JMSXGroupID", String.class);
		}
		
		String amqServiceName = exchange.getContext().resolvePropertyPlaceholders("{{activemq.service.name}}");

		String message =
				messageCount + " JMS Messages published to " +
				"[" + exchange.getProperty("destination", String.class) + "] " +
			    "in " + exchange.getProperty("duration") + " :\n" +
			    "- AMQ Service Name in OpenShift: " + amqServiceName + " \n" +
			    "- Message size: " + messageSize + " bytes\n" +
			    "- Delivery mode: " + deliveryMode + "\n" +
			    "- Message TTL (JMSExpiration): " + msgTTL + " ms\n";
		if (jmsxGroupID != null) 
			message += "- Message group (JMSXGroupID) : " + jmsxGroupID + "\n";
		message += "- Transaction batch size: " + transactionBatchSize + "\n" +
				   "- Transaction activated: " + transacted + "\n";
		exchange.getIn().setBody(message);
	}

}
