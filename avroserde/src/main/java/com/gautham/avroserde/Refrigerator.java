package com.gautham.avroserde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Refrigerator {

	private Map<Integer, List<Object>> shelves;

	public Refrigerator() {
		shelves = new HashMap<>();
		System.out.println("Refrigerator initialized!");
		for (Shelves shelf : Shelves.values()) {
			shelves.put(shelf.index(), new ArrayList<>());
			System.out.println(shelf.label() + " shelf with capacity of " + shelf.size() + " is deployed! Index - "
					+ shelf.index());
		}
	}

	public void put(Object object) {
		for (Integer i : shelves.keySet()) {
			if (shelves.get(i).size() < Shelves.sizeByIndex(i)) {
				List<Object> shelf = shelves.get(i);
				shelf.add(object);
				shelves.put(i, shelf);
				System.out.println("Object " + object + " has been added to " + Shelves.nameByIndex(i));
				break;
			}
			else {
				System.out.println(Shelves.nameByIndex(i) + " shelf is full!");
			}
		}
	}
	
	public Object get(Shelves shelf, Integer index) {
		try {
			return shelves.get(shelf.index()).get(index);	
		}
		catch(Exception e) {
			System.out.println("Object not found!" + e);
			return null;
		}
	}
}
