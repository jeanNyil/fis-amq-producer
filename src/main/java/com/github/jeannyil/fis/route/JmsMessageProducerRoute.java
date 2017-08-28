package com.github.jeannyil.fis.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageProducerRoute extends RouteBuilder {
	
	@SuppressWarnings("el-syntax")
	@Override
	public void configure() throws Exception {
		from("netty4-http:http://0.0.0.0:9090/produceJmsMessage")
			.routeId("jms-message-producer-route")
			.streamCaching()
			.setProperty("start", simple("${date:now:YYYYMMDD HH:mm:ss.SSS}"))
			.filter(header("destination").isNull())
				.transform().method("informationBean","transactedUsage()")
				.stop()
			.end()
			.process("jmsSendPropertiesProcessor") // Prepare JMS Send Properties
			.log(LoggingLevel.INFO, 
					"Publishing ${exchangeProperty.messageCount} messages...")
			.setExchangePattern(ExchangePattern.InOnly)
			.loopDoWhile(simple("${exchangeProperty.messageCount} > 0"))
				.process("jmsTransactionBatchProcessor") // Prepare Transaction Batch to send
				.log(LoggingLevel.INFO, 
						"Batch size: ${exchangeProperty.transactionBatchSize} - [${exchangeProperty.sjmsUri}]")
				.recipientList(exchangeProperty("sjmsUri")).end()
			.end()
			.setProperty("end", simple("${date:now:YYYYMMDD HH:mm:ss.SSS}"))
			.setProperty("duration").method("informationBean",
        				 "getDuration(${exchangeProperty.start},${exchangeProperty.end})")
			.log(LoggingLevel.INFO, 
					"Publishing ${exchangeProperty.messageToSend} messages DONE in ${exchangeProperty.duration}")
			.setExchangePattern(ExchangePattern.InOut)
			.process("prepareHttpResponseProcessor");
	}
	
}
