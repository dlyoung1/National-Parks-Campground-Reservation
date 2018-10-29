package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class JDBCParkDAOIntegrationTest {
	
	private static final int TEST_PARK = 4;
	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
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
		this.dao = new JDBCParkDAO(dataSource);
		String sqlInsertPark = "INSERT INTO park (park_id, name, location, establish_date, area, visitors, description) "
							+ "VALUES (?, 'NewPark', 'NewLocation', '2018-10-27', '1', '1', 'This is a new park created for this test. It serves no purpose outside of testing. If you read this description, you have too much time on your hands.') ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark, TEST_PARK);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returns_all_parks() {
		List<String> results = dao.getAllParks();
		assertNotNull(results);
		assertEquals(4, results.size());
	}
	
	@Test
	public void gets_park_information() {
		List<String> results = dao.getParkInfo("NewPark");
		assertEquals(1, results.indexOf("NewPark"));
		assertEquals("NewLocation", results.get(3));
		assertEquals("Location: ", results.get(2));
	}
	
	@Test
	public void returns_park_id() {
		int result = dao.getParkId("NewPark");
		assertEquals(4, result);
	}
	
	@Test
	public void returns_park_name() {
		String result = dao.getParkName(4);
		assertEquals("NewPark", result);
	}
	
}
