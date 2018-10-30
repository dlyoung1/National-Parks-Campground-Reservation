package com.techelevator.campground.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class JDBCCampgroundDAO implements CampgroundDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Campground> getAllCampgrounds(int parkId) {
		List<Campground> campgrounds = new ArrayList<Campground>();
		String sqlGetAllCampgrounds = "SELECT name FROM campground WHERE park_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetAllCampgrounds, parkId);
		while(results.next()) {
			String name = results.getString("name");
			Campground campground = new Campground();
			campground.setName(name);
			campgrounds.add(campground);
		}
		return campgrounds;
	}
	
	@Override
	public List<Campground> getCampgroundInformation(int parkId){
		List<Campground> campgrounds = new ArrayList<Campground>();
		String sqlGetCampgroundInformation = "SELECT name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetCampgroundInformation, parkId);
		while(results.next()) {
			String name = results.getString("name");
			int open = results.getInt("open_from_mm");
			String monthOpen = Month.of(open).name();
			int close = results.getInt("open_to_mm");
			String monthClose = Month.of(close).name();
			BigDecimal dailyFee = results.getBigDecimal("daily_fee");
			dailyFee = dailyFee.setScale(2, BigDecimal.ROUND_HALF_UP);
			Campground campground = new Campground();
			campground.setName(name);
			campground.setOpenFrom(monthOpen);
			campground.setOpenTo(monthClose);
			campground.setDailyFee(dailyFee);
			campgrounds.add(campground);
		}
		return campgrounds;
	}
	
	public List<String> getCampgroundId(int parkId) {
		List<String> campgrounds = new ArrayList<>();
		String sqlGetCampgroundInformation = "SELECT campground_id FROM campground WHERE park_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetCampgroundInformation, parkId);
		while(results.next()) {
			String campgroundId = results.getString("campground_id");
			campgrounds.add(campgroundId);
		}
		return campgrounds;
	}
	
	public int getCampgroundOpenMonth(int campgroundId) {
		int openMonth = 0;
		String sqlGetCampgroundOpenMonth = "SELECT open_from_mm FROM campground WHERE campground_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetCampgroundOpenMonth, campgroundId);
		while(results.next()) {
			openMonth = results.getInt("open_from_mm");
		}
		return openMonth;
	}
	
	public int getCampgroundCloseMonth(int campgroundId) {
		int closeMonth = 0;
		String sqlGetCampgroundOpenMonth = "SELECT open_to_mm FROM campground WHERE campground_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetCampgroundOpenMonth, campgroundId);
		while(results.next()) {
			closeMonth = results.getInt("open_to_mm");
		}
		return closeMonth;
	}
	
	@Override
	public BigDecimal getCampgroundFee(int campgroundId) {
		BigDecimal campgroundFee = new BigDecimal("0.20");
		String sqlGetCampgroundFee = "SELECT daily_fee FROM campground WHERE campground_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetCampgroundFee, campgroundId);
		while(results.next()) {
			campgroundFee = results.getBigDecimal("daily_fee");
			campgroundFee = campgroundFee.setScale(2, BigDecimal.ROUND_HALF_UP);
		}
		return campgroundFee;
	}

}
