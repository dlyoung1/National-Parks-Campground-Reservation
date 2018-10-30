package com.techelevator.campground;

import java.math.BigDecimal;
import java.util.List;

public interface CampgroundDAO {

	/**
	 * Returns a list of all camp grounds in a park
	 * @param parkId
	 * @return
	 */
	public List<Campground> getAllCampgrounds(int parkId);
	
	/**
	 * Returns a list of camp ground information based on park id
	 * @param parkId
	 * @return
	 */
	public List<Campground> getCampgroundInformation(int parkId);
	
	/**
	 * Gets the daily fee of given camp ground
	 * @param campgroundId
	 * @return
	 */
	
	/**
	 * Returns camp ground IDs of the park
	 * @param parkId
	 * @return
	 */
	public List<String> getCampgroundId(int parkId);
	
	public BigDecimal getCampgroundFee(int campgroundId);
	
	/**
	 * Returns the opening month of the selected camp ground
	 * @param campgroundId
	 * @return
	 */
	public int getCampgroundOpenMonth(int campgroundId);
	
	/**
	 * Returns the closing month of the selected camp ground
	 * @param campgroundId
	 * @return
	 */
	public int getCampgroundCloseMonth(int campgroundId);
	
}
