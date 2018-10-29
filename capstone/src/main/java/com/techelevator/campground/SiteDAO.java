package com.techelevator.campground;

import java.util.List;

public interface SiteDAO {
	
	/**
	 * Returns a list of sites within the chosen camp ground
	 * @param campgroundId
	 * @return
	 */
	public List<Site> getSitesByCampgroundId(int campgroundId);
	
	public Site getSiteInfo(int siteNum);

}
