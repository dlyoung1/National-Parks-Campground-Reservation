package com.techelevator.campground.jdbc;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import com.techelevator.campground.ReservationDAO;

public class JDBCReservationDAO implements ReservationDAO {
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Integer> getAvailableSites(int campgroundId, LocalDate arrivalDate, LocalDate departureDate) {
		LocalDate arrive = arrivalDate.minus(Period.ofDays(1));
		LocalDate depart = departureDate.plus(Period.ofDays(1));
		List<Integer> sites = new ArrayList<>();
		String sqlGetAvailableSites = "SELECT site_id FROM site WHERE " 
									+ "campground_id = ? AND site_id NOT IN "
									+ "(SELECT site_id FROM reservation " 
									+ "WHERE (to_date BETWEEN ? AND ? ) OR "
									+ "(from_date BETWEEN ? AND ?)) ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetAvailableSites, campgroundId, arrive, depart, arrive, depart);
		while(results.next()) {
			int siteId = results.getInt("site_id");
			sites.add(siteId);
		}
		return sites;
	}

}
