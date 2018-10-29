package com.techelevator.campground;

import java.util.List;

public interface ParkDAO {
	
	/**
	 * Returns a list of park names
	 * @return
	 */
	public List<String> getAllParks();

	/**
	 * Gets all park information and returns in a formatted list
	 * @param parkName
	 * @return
	 */
	public List<String> getParkInfo(String parkName);
	
	/**
	 * Returns id of park using name of park
	 * @param parkName
	 * @return
	 */
	public int getParkId(String parkName);
	
	/**
	 * Returns name of park using id of park
	 * @param parkId
	 * @return
	 */
	public String getParkName(int parkId);

}
