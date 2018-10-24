package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Project;

public class JDBCProjectDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCProjectDAO dao;

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
		this.dao = new JDBCProjectDAO(dataSource);
	}


	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returnActiveProjects() {
		List<Project> results = dao.getAllActiveProjects();
		assertNotNull(results);
		assertEquals(3, results.size());
	}
	
//	@Test
//	public void removeEmployee() {
//		dao.removeEmployeeFromProject((long)6, (long)4);
//	}
//	
//	@Test
//	public void addEmployee() {
//		dao.addEmployeeToProject((long)1, (long)4);
//	}

}
