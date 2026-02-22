package com.kk.rest.service;

import com.kk.rest.model.Employee;
import com.kk.rest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public List<Employee> getAllEmployees() {
		List<Employee> employees = employeeRepository.findAll();
		if (employees.isEmpty()) {
			throw new RuntimeException("No employees found in the database");
		}
		return employees;
	}

	public Optional<Employee> getEmployeeById(String id) {
		validateId(id);
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isEmpty()) {
			throw new RuntimeException("Employee with ID: " + id + " not found");
		}
		return employee;
	}

	public Employee saveEmployee(Employee employee) {
		validateEmployee(employee);
		Employee savedEmployee = employeeRepository.save(employee);
		return savedEmployee;
	}

	public Employee updateEmployee(String id, Employee employee) {
		validateId(id);
		Optional<Employee> existingEmployee = employeeRepository.findById(id);

		if (existingEmployee.isPresent()) {
			Employee emp = existingEmployee.get();

			if (employee.getEName() != null && !employee.getEName().trim().isEmpty()) {
				emp.setEName(employee.getEName().trim());
			}
			if (employee.getEEmail() != null && !employee.getEEmail().trim().isEmpty()) {
				validateEmail(employee.getEEmail());
				emp.setEEmail(employee.getEEmail().trim());
			}
			if (employee.getERole() != null && !employee.getERole().trim().isEmpty()) {
				emp.setERole(employee.getERole().trim());
			}
			return employeeRepository.save(emp);
		}
		throw new RuntimeException("Employee with ID: " + id + " not found");
	}

	public void deleteEmployee(String id) {
		validateId(id);
		if (!employeeRepository.existsById(id)) {
			throw new RuntimeException("Employee with ID: " + id + " does not exist");
		}
		employeeRepository.deleteById(id);
	}

	private void validateEmployee(Employee employee) {
		if (employee == null) {
			throw new IllegalArgumentException("Employee object cannot be null");
		}
		if (employee.getEName() == null || employee.getEName().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee name is required and cannot be empty");
		}
		if (employee.getEEmail() == null || employee.getEEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee email is required and cannot be empty");
		}
		validateEmail(employee.getEEmail());
		if (employee.getERole() == null || employee.getERole().trim().isEmpty()) {
			throw new IllegalArgumentException("Employee role is required and cannot be empty");
		}
	}

	private void validateId(String id) {
		if (id == null || id.trim().isEmpty()) {
			throw new IllegalArgumentException("Employee ID cannot be null or empty");
		}
	}

	private void validateEmail(String email) {
		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException("Invalid email format: " + email);
		}
	}
}

