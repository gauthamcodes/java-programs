package com.gautham.startstreams;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		Runnable task1 = new Runnable() {
			@Override
			public void run() {
				BankTransactions transactions = new BankTransactions();
				transactions.stream();
			}
		};

		Runnable task2 = new Runnable() {
			@Override
			public void run() {
				TransactionProducer producer = new TransactionProducer();
				producer.produce();
			}
		};
		
		Thread streamer = new Thread(task1);
		streamer.start();
		
		Thread producer = new Thread(task2);
		producer.start();		
	}
}
