package com.github.jeannyil.fis.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.sjms.BatchMessage;

@SuppressWarnings("deprecation")
public class JmsTransactionBatchProcessor implements Processor {

	@Override
	public void process(Exchange exchange) throws Exception {
		
		String messageToSend = generateMessagePayload(exchange.getProperty("messageSize", Integer.class));
		
		int batchSize = exchange.getProperty("transactionBatchSize", Integer.class);
		boolean transacted = exchange.getProperty("transacted", Boolean.class);
		int messageCount = exchange.getProperty("messageCount", Integer.class);
		
		// Avoid having batch size == 0
		if (batchSize == 0) { 
			batchSize = 1; 
			exchange.setProperty("transactionBatchSize", batchSize);
		}
		
		// Avoid batchSize > total number of message to process
		if  (batchSize > messageCount) {
			exchange.setProperty("transactionBatchSize", messageCount);
			batchSize = messageCount;
		}
		
		exchange.setProperty("messageCount", messageCount - batchSize);
		
		// Prepare message headers
		Map<String, Object> messageHeader = null;
		if (exchange.getProperty("JMSXGroupID") != null) {
			messageHeader = new HashMap<String, Object>();
			messageHeader.put("JMSXGroupID", exchange.getProperty("JMSXGroupID", String.class));
		}
		
		// Prepare batch messages
		List<BatchMessage<String>> messages = new ArrayList<BatchMessage<String>>();
		for (int i = 1; i <= batchSize; i++) {
			
		    BatchMessage<String> message = new BatchMessage<String>(messageToSend, messageHeader);
		    messages.add(message);
		}
		
		String destination = exchange.getProperty("destination", String.class);
		Long msgTTL = exchange.getProperty("msgTTL", Long.class);
		boolean persistent = exchange.getProperty("persistent", Boolean.class);
		
		String sjmsUri = "sjms:" + destination +
						 "?ttl=" + msgTTL + 
					     "&persistent=" + persistent +
						 "&transacted=" + transacted;
		exchange.setProperty("sjmsUri", sjmsUri);
		exchange.getIn().setBody(messages);
	}
	
	private String generateMessagePayload(int messageSize) {
		StringBuffer buffer = new StringBuffer(messageSize);
        buffer.append("Message sample : " + new Date());
        if (buffer.length() > messageSize) {
            return buffer.substring(0, messageSize);
        }
        for (int i = buffer.length(); i < messageSize; i++) {
            buffer.append(' ');
        }
        return buffer.toString();
	}
	
	

}
