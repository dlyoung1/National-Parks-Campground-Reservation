package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.Campground;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDBCCampgroundDAOIntegrationTest {
	
	private static final int TEST_CAMPGROUND = 8;
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO dao;

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
		this.dao = new JDBCCampgroundDAO(dataSource);
		String sqlInsertCampground = "INSERT INTO campground (campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee) "
									+ "VALUES (?, '3', 'New Campground', '02', '10', '20.00') ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertCampground, TEST_CAMPGROUND);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returns_all_campgrounds() {
		List<Campground> results = dao.getAllCampgrounds(3);
		assertEquals(2, results.size());
		
		assertEquals("New Campground", results.get(1).getName());
	}
	
	@Test
	public void gets_campground_info() {
		List<Campground> results = dao.getCampgroundInformation(3);
		assertEquals("New Campground", results.get(1).getName());
		
		assertEquals("OCTOBER", results.get(1).getOpenTo());
	}
	
	@Test
	public void returns_list_of_campground_ids() {
		List<String> results = dao.getCampgroundId(3);
		assertEquals(2, results.size());
	}
	
	@Test
	public void gets_campground_open_month() {
		int result = dao.getCampgroundOpenMonth(8);
		assertEquals(02, result);
	}
	
	@Test
	public void gets_campground_close_month() {
		int result = dao.getCampgroundCloseMonth(8);
		assertEquals(10, result);
	}
	
	@Test
	public void gets_campground_daily_fee() {
		BigDecimal result = dao.getCampgroundFee(8);
		BigDecimal actual = new BigDecimal (20.0).setScale(2, BigDecimal.ROUND_HALF_UP);
		assertEquals(actual, result);
	}
	
}
