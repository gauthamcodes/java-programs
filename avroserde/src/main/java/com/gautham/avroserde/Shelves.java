package com.gautham.avroserde;

public enum Shelves {
	SMALL("Small",0,3),MEDIUM("Medium",1,5),BIG("Big",2,9);
	
	private String label;
	private Integer index;
	private Integer size;
	
	Shelves(String label, Integer index, Integer size) {
		this.label = label;
		this.index = index;
		this.size = size;
	}
	
	public String label() {
		return label;
	}
	
	public Integer index() {
		return index;
	}
	
	public Integer size() {
		return size;
	}
	
	public static Integer sizeByIndex(Integer index) {
		Integer size = null;
		for(Shelves shelf:Shelves.values()) {
			if(shelf.index.equals(index)) {
				size = shelf.size();
				break;
			}
		}
		return size;
	}
	
	public static String nameByIndex(Integer index) {
		String name = null;
		for(Shelves shelf:Shelves.values()) {
			if(shelf.index.equals(index)) {
				name = shelf.name();
				break;
			}
		}
		return name;
	}
}
