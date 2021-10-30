package com.nashtech.MyBikeShop.DTO;

public class ReportProdProcess {
	private String id;
	private String name;
	private int quantity;
	private int inProcess;
	private int delivery;
	private int completed;
	private int cancel;
	
	public ReportProdProcess() {
		super();
	}

	public ReportProdProcess(String id, String name, int quantity, int inProcess, int delivery, int completed, int cancel) {
		super();
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.inProcess = inProcess;
		this.delivery = delivery;
		this.completed = completed;
		this.cancel = cancel;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getInProcess() {
		return inProcess;
	}

	public void setInProcess(int inProcess) {
		this.inProcess = inProcess;
	}

	public int getDelivery() {
		return delivery;
	}

	public void setDelivery(int delivery) {
		this.delivery = delivery;
	}

	public int getCompleted() {
		return completed;
	}

	public void setCompleted(int completed) {
		this.completed = completed;
	}

	public int getCancel() {
		return cancel;
	}

	public void setCancel(int cancel) {
		this.cancel = cancel;
	}
	
	
}
