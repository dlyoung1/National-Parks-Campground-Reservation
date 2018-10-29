package com.techelevator.campground;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

	/**
	 * Searches reservation table and returns a list of camp ground sites that are available during the time specified
	 * @param campgroundId
	 * @param arrivalDate
	 * @param departureDate
	 * @return
	 */
	public List<Integer> getAvailableSites(int campgroundId, LocalDate arrivalDate, LocalDate departureDate);
	
}
