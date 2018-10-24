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
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;

public class JDBCDepartmentDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCDepartmentDAO dao;

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
		this.dao = new JDBCDepartmentDAO(dataSource);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returnAllDepartments() {
		List<Department> results = dao.getAllDepartments();
		assertNotNull(results);
		assertEquals(4, results.size());
	}
	
	@Test
	public void returnNameOfDepartment() {
		Department actual = new Department();
		actual.setName("Store Support");
		List<Department> actualList = new ArrayList<>();
		actualList.add(actual);
		List<Department> results = dao.searchDepartmentsByName("Store Support");
		assertEquals(actualList.toString(), results.toString());
		
		results = dao.searchDepartmentsByName("New Dept");
		assertTrue(results.isEmpty());
	}
	
//	@Test
//	public void updateDepartmentName() {
//		List<Department> results = dao.saveDepartment(updatedDepartment);
//	}
	
//	@Test
//	public void createNewDepartment() {
//		Department actual = new Department();
//	}
	
//	@Test
//	public void returnDepartmentById() {
//		Department actual = new Department();
//		actual.setId((long)4);
//		Department results = dao.getDepartmentById((long)4);
//		assertEquals(actual.toString(), results.toString());
//	}

}
