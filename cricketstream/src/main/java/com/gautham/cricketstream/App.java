package com.gautham.cricketstream;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InterruptedException {
		BasicConfigurator.configure(new NullAppender());

		SimpleConsumer consumer = new SimpleConsumer();
		Thread consumeTask = new Thread(new Runnable() {
			@Override
			public void run() {
				consumer.consume();
			}
		});
		consumeTask.start();

		SimpleProducer producer = new SimpleProducer();
		Thread producerTask = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					producer.produce();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		producerTask.start();

		SimpleStreamer streamer = new SimpleStreamer();
		Thread streamTask = new Thread(new Runnable() {
			@Override
			public void run() {
				streamer.stream();
			}
		});
		streamTask.start();

	}
}
