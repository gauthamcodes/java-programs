package com.gautham.filevalidator.model;

public class Unknown {
	private Integer id;
	private String data;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Unknown [id=" + id + ", data=" + data + "]";
	}

}
