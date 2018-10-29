package com.techelevator.campground.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Site;
import com.techelevator.campground.SiteDAO;

public class JDBCSiteDAO implements SiteDAO{
	
	private JdbcTemplate jdbcTemplate;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Site getSiteInfo(int siteNum) {
		Site site = new Site();
		String sqlGetSiteById = "SELECT site_number, max_occupancy, accessible, max_rv_length, utilities FROM site WHERE site_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetSiteById, siteNum);
		while(results.next()) {
			int siteNumber = results.getInt("site_number");
			int maxOccupancy = results.getInt("max_occupancy");
			boolean accessible = results.getBoolean("accessible");
			int maxRVLength = results.getInt("max_rv_length");
			boolean utilities = results.getBoolean("utilities");
			site.setSiteNumber(siteNumber);
			site.setMaxOccupancy(maxOccupancy);
			site.setAccessible(accessible);
			site.setRvLength(maxRVLength);
			site.setUtilities(utilities);
		}
		return site;
	}
	
	@Override
	public List<Site> getSitesByCampgroundId(int campgroundId) {
		List<Site> sites = new ArrayList<Site>();
		String sqlGetSitesByCampgroundId = "SELECT site_number FROM site WHERE campground_id = ? ";
		SqlRowSet results = this.jdbcTemplate.queryForRowSet(sqlGetSitesByCampgroundId, campgroundId);
		while(results.next()) {
			int siteId = results.getInt("site_number");
			Site site = new Site();
			site.setSiteId(siteId);
			sites.add(site);
		}
		return sites;
	}

}
