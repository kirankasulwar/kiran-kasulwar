package com.kk.rest.controller;

import com.kk.rest.model.Employee;
import com.kk.rest.service.EmployeeService;
import com.kk.rest.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees and uploading files to external service")
public class RestServiceController {
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping("/upload")
	@Operation(summary = "Upload file to external service", description = "Uploads a file to the external S3 service with application metadata")
	@ApiResponse(responseCode = "200", description = "File uploaded successfully", content = @Content(schema = @Schema(implementation = Map.class)))
	@ApiResponse(responseCode = "400", description = "Bad request - missing or invalid parameters")
	@ApiResponse(responseCode = "500", description = "Internal server error")
	public ResponseEntity<Map<String, Object>> uploadFileToExternalService(
			@RequestParam("application-id") String applicationId,
			@RequestParam("file") MultipartFile file) {
		try {
			Map<String, Object> response = fileUploadService.uploadFileToExternalService(applicationId, file);
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			Map<String, Object> errorResponse = new java.util.HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (IOException e) {
			Map<String, Object> errorResponse = new java.util.HashMap<>();
			errorResponse.put("success", false);
			errorResponse.put("message", "Failed to upload file: " + e.getMessage());
			return ResponseEntity.status(500).body(errorResponse);
		}
	}

	@GetMapping
	@Operation(summary = "Get all employees", description = "Retrieves a list of all employees from the database")
	@ApiResponse(responseCode = "200", description = "List of employees retrieved successfully", content = @Content(schema = @Schema(implementation = Employee.class)))
	@ApiResponse(responseCode = "500", description = "No employees found in the database")
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get employee by ID", description = "Retrieves a specific employee by their ID")
	@ApiResponse(responseCode = "200", description = "Employee found", content = @Content(schema = @Schema(implementation = Employee.class)))
	@ApiResponse(responseCode = "404", description = "Employee not found")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
		Optional<Employee> employee = employeeService.getEmployeeById(id);
		return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping
	@Operation(summary = "Create a new employee", description = "Creates a new employee record in the database")
	@ApiResponse(responseCode = "200", description = "Employee created successfully", content = @Content(schema = @Schema(implementation = Employee.class)))
	@ApiResponse(responseCode = "400", description = "Invalid employee data")
	public ResponseEntity<Employee> createEmployee(@RequestBody(description = "Employee details to create", required = true) Employee employee) {
		Employee saved = employeeService.saveEmployee(employee);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete an employee", description = "Deletes an employee record by their ID")
	@ApiResponse(responseCode = "200", description = "Employee deleted successfully")
	@ApiResponse(responseCode = "404", description = "Employee not found")
	public ResponseEntity<Void> deleteEmployee(@PathVariable String id) {
		employeeService.deleteEmployee(id);
		return ResponseEntity.ok().build();
	}
}
