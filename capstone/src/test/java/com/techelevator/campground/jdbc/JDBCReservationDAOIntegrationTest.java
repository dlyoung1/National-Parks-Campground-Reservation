package com.techelevator.campground.jdbc;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class JDBCReservationDAOIntegrationTest {
	
	private static final int TEST_RESERVATION = 45;
	private static SingleConnectionDataSource dataSource;
	private JDBCReservationDAO dao;

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
		this.dao = new JDBCReservationDAO(dataSource);
		String sqlInsertReservation = "INSERT INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "
									+ "VALUES (?, '47', 'Test', '2018-10-27', '2018-10-30', '2018-10-26') ";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertReservation, TEST_RESERVATION);
	}

	@After
	public void tearDown() throws Exception {
		dataSource.getConnection().rollback();
	}

	@Test
	public void searches_reservations_and_returns_available_sites() {
		List<Integer> results = dao.getAvailableSites(1, LocalDate.of(2018, 10, 10), LocalDate.of(2018, 10, 20));
		assertEquals(true, results.contains(47));
	}
	
	@Test
	public void searches_reservations_and_omits_reserved_sites() {
		List<Integer> results = dao.getAvailableSites(1, LocalDate.of(2018, 10, 28), LocalDate.of(2018, 10, 29));
		Assert.assertEquals(false, results.contains(47));
	}

}
