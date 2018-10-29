package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.Site;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDBCSiteDAOIntegrationTest {
	
	private static final int TEST_SITE = 623;
	private static SingleConnectionDataSource dataSource;
	private JDBCSiteDAO dao;

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
		this.dao = new JDBCSiteDAO(dataSource);
		String sqlInsertSite = "INSERT INTO site (site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities) "
							+ "VALUES(?, '7', '6', '4', 'false', '0', 'false') ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertSite, TEST_SITE);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void returns_site_information() {
		Site result = dao.getSiteInfo(TEST_SITE);
		assertEquals(6, result.getSiteNumber());
		
		assertEquals(0, result.getRvLength());
		
		assertEquals(4, result.getMaxOccupancy());
	}
	
	@Test
	public void returns_sites_by_campgroundId() {
		List<Site> results = dao.getSitesByCampgroundId(7);
		assertEquals(6, results.size());
	}

}
