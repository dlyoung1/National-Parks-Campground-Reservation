package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.CampgroundDAO;
import com.techelevator.campground.ParkDAO;
import com.techelevator.campground.SiteDAO;
import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {
	
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	private static final String[] MAIN_MENU_OPTIONS = new String[] {//input from park table + QUIT
																	};
	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	private static final String PARK_MENU_OPTION_VIEW = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH = "Search for reservation";
	private static final String[] PARK_MENU_OPTIONS = new String[] {PARK_MENU_OPTION_VIEW, 
																	PARK_MENU_OPTION_SEARCH,
																	MENU_OPTION_RETURN_TO_MAIN};
	private static final String CAMPGROUND_OPTION_SEARCH = "Search for reservation";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] {CAMPGROUND_OPTION_SEARCH,
																		MENU_OPTION_RETURN_TO_MAIN};
	
	
	private Menu menu;
	private CampgroundDAO campgroundDAO;
	private ParkDAO parkDAO;
	private SiteDAO siteDAO;

	public static void main(String[] args) {
		
		
		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();
	}

	public CampgroundCLI(DataSource datasource) {
		this.menu = new Menu(System.in, System.out);
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
//		campgroundDAO = new JDBCCampgroundDAO(dataSource);
//		parkDAO = new JDBCParkDAO(dataSource);
//		siteDAO = new JDBCSiteDAO(dataSource);
		
	}
	
	private void run() {
		while(true) {
			System.out.println("Select a Park");
			String choice = (String)menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

//			if(choice.equals(park.name)) {
//				handleParks();
//			} else if(choice.equals(park.name)){
//				handleParks();
//			} else if(choice.equals(park.name)){
//				handleParks();
//			} else if(choice.equals(MAIN_MENU_OPTION_QUIT)){
//				System.exit(0);
		}
	}
	
	private void handleParks() {
		System.out.println("Park Information");
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
//		if(choice.equals(anObject))
//	}
}
