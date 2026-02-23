package com.kk.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Rest-Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
	@Id
	private String id;
	private String EName;
	private String EEmail;
	private String ERole;
}
