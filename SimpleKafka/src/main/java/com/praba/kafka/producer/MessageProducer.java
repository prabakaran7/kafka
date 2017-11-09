package com.praba.kafka.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * This helper class push the message to Kafka Server
 * 
 * @author Praba
 *
 */
public class MessageProducer {

	/** HOST and PORT of Kafka Server */
	private static final String HOST = System.getProperty("KAFKA_HOST",
			"localhost:9092");

	/** Required properties to connect producer */
	private static final Properties configProperties;
	static {
		// Configure the Producer
		configProperties = new Properties();
		configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
		configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

	}

	/**
	 * This method publish the message to Kafka Server. For thread safety, this
	 * is synchronized. TODO: need to re-evaluate the synchronized later.
	 * 
	 * @param message
	 */
	public static synchronized void publish(String message, String topicName) {
		KafkaProducer<String, String> producer = null;
		try {
			producer = new KafkaProducer<String, String>(configProperties);

			ProducerRecord<String, String> rec = new ProducerRecord<String, String>(
					topicName, message);
			producer.send(rec);
		} finally {
			if (null != producer) {
				producer.close();
			}
		}

	}
}
