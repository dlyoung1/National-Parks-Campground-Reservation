package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Employee;

public class JDBCEmployeeDAOIntegrationTest {
	
	private static final int TEST_EMPLOYEE = 13;
	private static SingleConnectionDataSource dataSource;
	private JDBCEmployeeDAO dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		dataSource.destroy();
	}

	@Before
	public void setUp() throws Exception {
		this.dao = new JDBCEmployeeDAO(dataSource);
		String sqlInsertEmployee = "INSERT INTO employee (employee_id, first_name, last_name, birth_date, gender, hire_date) VALUES (?, 'Action', 'Jackson', '1964-01-01', 'M', '2017-10-10') ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertEmployee, TEST_EMPLOYEE);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returnsAllEmployees() {
		List<Employee> results = dao.getAllEmployees();
		assertNotNull(results);
		assertEquals(13, results.size());
	}
	
	@Test
	public void returnsEmployeeName() {
		String first = "Action";
		String last = "Jackson";
		List<String> actual = new ArrayList<>();
		actual.add(last);
		actual.add(first);
		List<Employee> results = dao.searchEmployeesByName(first, last);
		assertEquals(actual.toString(), results.toString());
		
		first = "Waldo";
		last = "Fredrickson";
		actual = new ArrayList<>();
		actual.add(last);
		actual.add(first);
		results = dao.searchEmployeesByName(first, last);
		assertNotEquals(actual.toString(), results.toString());
	}
	
	@Test
	public void returnsEmployeesByDepartment() {
		Employee actual = getEmployee("Flo", "Henderson", 1);
		List<Employee> actualList = new ArrayList<>();
		actualList.add(actual);
		List<Employee> results = dao.getEmployeesByDepartmentId(1);
		assertEquals(actualList.toString(), results.toString());
		
		actual = getEmployee("Jarred", "Lukach", 4);
		actualList = new ArrayList<>();
		actualList.add(actual);
		actual = getEmployee("Gabreila", "Christie", 4);
		actualList.add(actual);
		results = dao.getEmployeesByDepartmentId(4);
		assertEquals(actualList.toString(), results.toString());
	}
	
	@Test
	public void returnsEmployeesWithoutProject() {
		Employee actual = getEmployee("Delora", "Coty", 2);
		List<Employee> actualList = new ArrayList<>();
		actualList.add(actual);
		actual = getEmployee("Gabreila", "Christie", 4);
		actualList.add(actual);
		actual = getEmployee("Action", "Jackson", 0);
		actualList.add(actual);
		List<Employee> results = dao.getEmployeesWithoutProjects();
		assertEquals(actualList.toString(), results.toString());
	}
	
	@Test
	public void returnsEmployeesByProjectId() {
		List<Employee> actualList = new ArrayList<>();
		Employee actual = getEmployee("Franklin", "Trumbauer", 2);
		actualList.add(actual);
		actual = getEmployee("Sid", "Goodman", 2);
		actualList.add(actual);
		List<Employee> results = dao.getEmployeesByProjectId((long)1);
		assertEquals(actualList.toString(), results.toString());
	}
	
//	@Test
//	public void changesEmployeeDepartmentId() {
//		dao.changeEmployeeDepartment((long)13, (long)0);
//		Employee result = getEmployee("Action", "Jackson", 0);
//		assertEquals(0, result.getDepartmentId());
//	}
	

	private Employee getEmployee(String firstName, String lastName, long departmentId) {
		Employee theEmployee = new Employee();
		theEmployee.setFirstName(firstName);
		theEmployee.setLastName(lastName);
		theEmployee.setDepartmentId(departmentId);
		return theEmployee;
	}
	
}
