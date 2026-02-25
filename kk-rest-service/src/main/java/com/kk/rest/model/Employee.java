package com.kk.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "Rest-Employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Employee Information")
public class Employee {
	@Id
	@Schema(description = "Unique identifier for the employee", example = "507f1f77bcf86cd799439011")
	private String id;

	@Schema(description = "Employee name", example = "John Doe")
	private String EName;

	@Schema(description = "Employee email address", example = "john.doe@example.com")
	private String EEmail;

	@Schema(description = "Employee role/position", example = "Software Engineer")
	private String ERole;
}


