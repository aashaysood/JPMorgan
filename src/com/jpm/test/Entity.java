/**
 * 
 */
package com.jpm.test;

/**
 * @author Aashay
 *
 */
public class Entity {
	
	public String name;
	public double amount;
	
	public Entity(String name, double amount){
		this.name=name;
		this.amount=amount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
	
}
