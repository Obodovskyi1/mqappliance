package com.ibm.aj.simple;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

public class Sender {

	public static void main(String[] args) {
		String hostname = "9.20.194.189";
		int port = 1987;
		String channel = "CLOUD.APP.SVRCONN";
		String qm = "QM.AJ";
		String senderQ = "DEV.QUEUE.1";
		String APP_USER = "aj"; // User name that application uses to connect to MQ
		String APP_PASSWORD = "W7aqri52R1I7Hyv9R_Py-qMJb81AKa3Ve7H0robV73WF"; // Password that the application uses to connect to MQ
		int noMsgs = 1;

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

		System.out.println(dateFormat.format(new Date()) + ": Connection to " + qm + " at " + hostname
				+ " via channel: " + channel);

		try {

			MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, qm);
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, hostname);
			cf.setIntProperty(WMQConstants.WMQ_PORT, port);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
			cf.setStringProperty(WMQConstants.USERID, APP_USER);
			cf.setStringProperty(WMQConstants.PASSWORD, APP_PASSWORD);
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);

			JMSContext context = cf.createContext();
			System.out.println(dateFormat.format(new Date()) + ": Connection established to send a message");

			JMSProducer prod = context.createProducer();
			Queue senderQueue = context.createQueue(senderQ);

			for (int i = 1; i <= noMsgs; i++) {
				TextMessage textMessage = context.createTextMessage();
				textMessage.setText("AJMsg-"+dateFormat.format(new Date())+"-" + Integer.toString(i));
				prod.send(senderQueue, textMessage);
			}

			System.out.println(dateFormat.format(new Date()) + ": All Messages Sent!");

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
