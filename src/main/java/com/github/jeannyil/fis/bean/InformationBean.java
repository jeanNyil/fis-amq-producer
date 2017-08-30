package com.github.jeannyil.fis.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationBean {
	public Date convertStringToDate(String dateString) {
	      Date formatteddate = null;
	      DateFormat df = new SimpleDateFormat("YYYYMMDD HH:mm:ss.SSS");
	      try{
	    	  formatteddate = df.parse(dateString);
	      }
	      catch ( Exception ex ){
	    	  return null;
	      }
	      return formatteddate;
	}
	
	public String getDuration(String Date_in, String Date_end) throws Exception {
		  Date date_in_c = null;
		  Date date_end_c = null;
		  try { 
			  if(Date_in != null && !Date_in.isEmpty()  &&  Date_end != null && !Date_end.isEmpty())  
				  {
				  		date_in_c=convertStringToDate(Date_in);
				  		date_end_c=convertStringToDate(Date_end);
				  		long diff = date_end_c.getTime() -  date_in_c.getTime();
				  		String s = Long.toString(diff) + "ms";
				  	return s;	  
				  }
			  else
			  {
				  return "N/A";
			  }
		  	} catch (Exception e) 
		  		{
		  	      return "N/A";
		  	}
	 }
	
	public String transactedUsage() {
		String usage = 
			"Missing mandatory parameter: destination \n" +
			"Usage: \n" +
			"http://<host>:<service_port>/produceJmsMessage?[OPTIONS]\n" +
			"Options:\n" +
			"[destination         queue:.. | topic:..] - producer destination; mandatory\n" +
			"[messageCount                          N] - number of messages to send; default 1000\n" +
			"[messageSize                           N] - message size in bytes; default 5120 text message\n" +
			"[msgTTL                                N] - message TTL in milliseconds; default 86400000\n" +
			"[msgGroupID                           ..] - JMS message group identifier\n" +
			"[persistent                 true | false] - use persistent or non persistent messages; default true\n" +
			"[transacted                 true | false] - use transaction in sending JMS messages; default true\n" +
			"[transactionBatchSize                  N] - use to send transaction batches of size N; default 10\n\n" +
			"Example:\n" + 
			"http --timeout=120 'http://jms-service.apps.ocp.rhlab.ovh/producejmsmessage?destination=queue:TRANSACTED.TEST1&messageCount=50000&transactionBatchSize=100'\n";
		return usage;
	}
}
