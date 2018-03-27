package com.gautham.filevalidator;

import java.io.File;
import java.io.IOException;

import com.gautham.filevalidator.kafka.SampleConsumer;
import com.gautham.filevalidator.kafka.SampleProducer;
import com.gautham.filevalidator.utils.FileUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {

		Runnable consumerTask = () -> {
			SampleConsumer consumer = new SampleConsumer();
			consumer.consume("gauthamtxt");
		};

		@SuppressWarnings("static-access")
		Runnable producerTask = () -> {
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			SampleProducer producer = new SampleProducer();
			File[] listOfFiles = new File(Config.FILE_DIRECTORY).listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile() && FileUtil.getFileExtension(listOfFiles[i]).equals(Config.FILE_TYPE)) {
					System.out.println(listOfFiles[i].getName());
					System.out.println(FileUtil.isPatternMatch(listOfFiles[i].getName()));
					if (FileUtil.isPatternMatch(listOfFiles[i].getName())) {
						try {
							producer.produce(Config.STUDENT_TOPIC,
									FileUtil.read(Config.FILE_DIRECTORY + listOfFiles[i].getName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		};

		Thread thread1 = new Thread(consumerTask);
		//Thread thread2 = new Thread(producerTask);
		thread1.start();
		//thread2.start();

	}
}
