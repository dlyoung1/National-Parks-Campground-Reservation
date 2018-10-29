package com.techelevator.campground.jdbc;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.techelevator.campground.ParkDAO;

public class JDBCParkDAO implements ParkDAO{
	
	public NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	private JdbcTemplate jdbcTemplate;
	public DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	
	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<String> getAllParks() {
		List<String> parks = new ArrayList<String>();
		String sqlGetAllParks = "SELECT name FROM park ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetAllParks);
		while(results.next()) {
			String name = results.getString("name");
			parks.add(name);
		}
		return parks;
	}

	@Override
	public List<String> getParkInfo(String parkName) {
		List<String> parks = new ArrayList<String>();
		String sqlGetParkInfo = "SELECT name, location, establish_date, area, visitors, description FROM park WHERE name = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetParkInfo, parkName);
		while(results.next()) {
			String name = results.getString("name");
			String location = results.getString("location");
			String establishDate = dateFormat.format(results.getDate("establish_date"));
			String area = numberFormat.format(results.getInt("area"));
			String visitors = numberFormat.format(results.getInt("visitors"));
			String description = results.getString("description");
			parks.add("Name: ");
			parks.add(name);
			parks.add("Location: ");
			parks.add(location);
			parks.add("Date established: ");
			parks.add(establishDate);
			parks.add("Area: ");
			parks.add(area);
			parks.add("Visitors: ");
			parks.add(visitors);
			parks.add("Description: ");
			int index = 0;
			int length = 100;
			while(index < description.length()) {
				String descriptionSubString = description.substring(index, Math.min(index + length,  description.length()));
				if(descriptionSubString.startsWith(" ")) {
					index += 1;
				}
				if(descriptionSubString.endsWith(" ") || descriptionSubString.endsWith(".") ) {
					parks.add(description.substring(index, Math.min(index + length, description.length())));
					parks.add(" ");
					index += (length);
					length = 100;
				} else {
					length += 1;
				}
				
			}
		}
		return parks;
	}
	
	@Override
	public int getParkId(String parkName) {
		int parkId = 0;
		String sqlGetParkId = "SELECT park_id FROM park WHERE name = ? ";
		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlGetParkId, parkName);
		while(result.next()) {
			parkId = result.getInt("park_id");
		}
		return parkId;
	}
	
	@Override
	public String getParkName(int parkId) {
		String parkName = null;
		String sqlGetParkName = "SELECT name FROM park WHERE park_id = ? ";
		SqlRowSet result = this.jdbcTemplate.queryForRowSet(sqlGetParkName, parkId);
		while(result.next()) {
			parkName = result.getString("name");
		}
		return parkName;
	}


}
