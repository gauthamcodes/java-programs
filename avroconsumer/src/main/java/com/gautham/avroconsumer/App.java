package com.gautham.avroconsumer;

import org.apache.log4j.BasicConfigurator;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		BasicConfigurator.configure();
		AvroStreamer.stream();
	}
}
