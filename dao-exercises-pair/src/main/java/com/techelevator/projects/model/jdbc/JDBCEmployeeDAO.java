package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		String sqlGetAllEmployees = "SELECT last_name, first_name FROM employee ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllEmployees);
		while(results.next()) {
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			Employee employee = new Employee();
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employees.add(employee);
		}
		return employees;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employees = new ArrayList<Employee>();
		String sqlSearchEmployeesByName = "SELECT first_name, last_name FROM employee WHERE " + "first_name ILIKE ? OR last_name ILIKE ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByName, firstNameSearch, lastNameSearch);
		while(results.next()) {
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			Employee theEmployee = new Employee();
			theEmployee.setFirstName(firstName);
			theEmployee.setLastName(lastName);
			employees.add(theEmployee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> employees = new ArrayList<Employee>();
		String sqlGetEmployeesByDepartmentId = "SELECT first_name, last_name FROM employee JOIN department ON department.department_id = employee.department_id WHERE " +
											   "department.department_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByDepartmentId, id);
		while(results.next()) {
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			Employee theEmployee = new Employee();
			theEmployee.setFirstName(firstName);
			theEmployee.setLastName(lastName);
			employees.add(theEmployee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> employees = new ArrayList<Employee>();
		String sqlGetEmployeesWithoutProjects = "SELECT first_name, last_name FROM employee " + 
												"WHERE employee_id NOT IN (SELECT employee_id FROM project_employee) ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesWithoutProjects);
		while(results.next()) {
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			Employee theEmployee = new Employee();
			theEmployee.setFirstName(firstName);
			theEmployee.setLastName(lastName);
			employees.add(theEmployee);
		}
		return employees;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> employees = new ArrayList<Employee>();
		String sqlGetEmployeesByProjectId = "SELECT first_name, last_name FROM employee JOIN project_employee ON employee.employee_id = project_employee.employee_id WHERE project_employee.project_id = ? ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByProjectId, projectId);
		while(results.next()) {
			Employee theEmployee = new Employee();
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			theEmployee.setFirstName(firstName);
			theEmployee.setLastName(lastName);
			employees.add(theEmployee);
		}
		return employees;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlChangeEmployeeDepartment = "UPDATE employee " + 
				"SET department_id = ? WHERE employee_id = ? ";
		this.jdbcTemplate.update(sqlChangeEmployeeDepartment, departmentId, employeeId);
	}


}
