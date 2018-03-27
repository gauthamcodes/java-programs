package com.gautham.multithreading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ExecutorService service = Executors.newFixedThreadPool(10);        
        MyCallable callable = new MyCallable();        
        List<Future<Integer>> list = new ArrayList<>();        
        for(int i=0;i<100;i++) {
        	list.add(service.submit(callable));
        }
        for(Future<Integer> fut : list){
            try {
                System.out.println(new Date()+ "::"+fut.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
    }
}
