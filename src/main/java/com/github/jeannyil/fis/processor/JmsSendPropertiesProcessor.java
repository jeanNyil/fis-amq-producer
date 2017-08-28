package com.github.jeannyil.fis.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class JmsSendPropertiesProcessor implements Processor {
	
	private int messageCount;
	private int transactionBatchSize;
	private boolean transacted;
	private int messageSize;
	private long msgTTL;
	private boolean persistent;

	@Override
	public void process(Exchange exchange) throws Exception {
		Message requestMessage = exchange.getIn();	
		if (requestMessage.getHeader("messageCount") != null) {
			exchange.setProperty("messageCount", requestMessage.getHeader("messageCount", Integer.class));
			exchange.setProperty("messageToSend", requestMessage.getHeader("messageCount", Integer.class));
		} else {
			exchange.setProperty("messageCount", messageCount);
			exchange.setProperty("messageToSend", messageCount);
		}
		
		if (requestMessage.getHeader("messageSize") != null) {
			exchange.setProperty("messageSize", requestMessage.getHeader("messageSize", Integer.class));
		} else {
			exchange.setProperty("messageSize", messageSize);
		}
		
		if (requestMessage.getHeader("transactionBatchSize") != null) {
			exchange.setProperty("transactionBatchSize", requestMessage.getHeader("transactionBatchSize", Integer.class));
			exchange.setProperty("requestTransactionBatchSize", requestMessage.getHeader("transactionBatchSize", Integer.class));
		} else {
			exchange.setProperty("transactionBatchSize", transactionBatchSize);
		}
		
		if (requestMessage.getHeader("transacted") != null) {
			exchange.setProperty("transacted", requestMessage.getHeader("transacted", Boolean.class));
		} else {
			exchange.setProperty("transacted", transacted);
		}
		
		if (requestMessage.getHeader("msgTTL") != null) {
			exchange.setProperty("msgTTL", requestMessage.getHeader("msgTTL",Long.class));
		} else {
			exchange.setProperty("msgTTL", msgTTL);
		}
		
		if (requestMessage.getHeader("persistent") != null) {
			exchange.setProperty("persistent", requestMessage.getHeader("persistent", Boolean.class));
		} else {
			exchange.setProperty("persistent", persistent);
		}
		
		if (requestMessage.getHeader("msgGroupID") != null)
			exchange.setProperty("JMSXGroupID", requestMessage.getHeader("msgGroupID", String.class));
		
		exchange.setProperty("destination", requestMessage.getHeader("destination",String.class));
	}
	
	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	
	public int getTransactionBatchSize() {
		return transactionBatchSize;
	}

	public void setTransactionBatchSize(int transactionBatchSize) {
		this.transactionBatchSize = transactionBatchSize;
	}
	
	public boolean isTransacted() {
		return transacted;
	}

	public void setTransacted(boolean transacted) {
		this.transacted = transacted;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public long getMsgTTL() {
		return msgTTL;
	}

	public void setMsgTTL(long msgTTL) {
		this.msgTTL = msgTTL;
	}

	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}
}
