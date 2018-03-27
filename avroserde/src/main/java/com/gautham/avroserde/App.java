package com.gautham.avroserde;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //AvroGenericSerDe serde = new AvroGenericSerDe("test");
    	Refrigerator ref = new Refrigerator();
    	ref.put(1);
    	System.out.println(ref.get(Shelves.SMALL, 2));
    	ref.put(2);
    	ref.put(1);
    	ref.put(2);
    	ref.put(1);
    	ref.put(2);
    	ref.put(1);
    	ref.put(2);
    	System.out.println(ref.get(Shelves.MEDIUM, 2));
    }
}
