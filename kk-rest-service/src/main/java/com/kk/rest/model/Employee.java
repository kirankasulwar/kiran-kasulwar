package com.kk.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Rest-Employee")
public class Employee {
	@Id
	private String id;
	private String EName;
	private String EEmail;
	private String ERole;

	public Employee() {}

	public Employee(String EName, String EEmail, String ERole) {
		this.EName = EName;
		this.EEmail = EEmail;
		this.ERole = ERole;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getEName() { return EName; }
	public void setEName(String EName) { this.EName = EName; }
	public String getEEmail() { return EEmail; }
	public void setEEmail(String EEmail) { this.EEmail = EEmail; }
	public String getERole() { return ERole; }
	public void setERole(String ERole) { this.ERole = ERole; }
}

