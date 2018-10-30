package com.techelevator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.dbcp2.BasicDataSource;
import com.techelevator.campground.Campground;
import com.techelevator.campground.CampgroundDAO;
import com.techelevator.campground.ParkDAO;
import com.techelevator.campground.ReservationDAO;
import com.techelevator.campground.Site;
import com.techelevator.campground.SiteDAO;
import com.techelevator.campground.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.jdbc.JDBCParkDAO;
import com.techelevator.campground.jdbc.JDBCReservationDAO;
import com.techelevator.campground.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {
//	Menu variables
	private static final String MAIN_MENU_OPTION_QUIT = "Quit";
	
	private static final String MENU_OPTION_RETURN_TO_MAIN = "Return to main menu";
	private static final String PARK_MENU_OPTION_VIEW = "View Campgrounds";
	private static final String PARK_MENU_OPTION_SEARCH = "Search for reservation";
	private static final String[] PARK_MENU_OPTIONS = new String[] {PARK_MENU_OPTION_VIEW, 
																	PARK_MENU_OPTION_SEARCH,
																	MENU_OPTION_RETURN_TO_MAIN};
	
	private static final String SITE_OPTION_RESERVE = "Reserve a site listed above";
	private static final String SITE_OPTION_NEXT = "Display next 5 sites";
	private static final String[] ALT_SITE_MENU_OPTIONS = new String[] {SITE_OPTION_RESERVE, MENU_OPTION_RETURN_TO_MAIN};
	private static final String[] SITE_MENU_OPTIONS = new String[] {SITE_OPTION_RESERVE,
																	SITE_OPTION_NEXT,
																	MENU_OPTION_RETURN_TO_MAIN};

	private Menu menu;
	private CampgroundDAO campgroundDAO;
	private ParkDAO parkDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public static void main(String[] args) throws Exception {
		CampgroundCLI application = new CampgroundCLI();
		application.run();
	}

	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		parkDAO = new JDBCParkDAO(dataSource);
		siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}
	
	private void run() throws Exception {
		while(true) {
			System.out.println("Select a Park");
			List<String> allParks = parkDAO.getAllParks();
			allParks.add(MAIN_MENU_OPTION_QUIT);
			String choice = (String)menu.getChoiceFromOptions(allParks.toArray());
			for(int i = 0; i < allParks.size(); i++) {
				if(choice.equals(allParks.get(i))) {
					String parkName = allParks.get(i);
					handleParks(parkName);
				} else if(choice.equals(MAIN_MENU_OPTION_QUIT)){
					System.exit(0);
				}
			}
		}
	}
	
	private void listParkInfo(String park) {
		List<String> parkInfo = parkDAO.getParkInfo(park);
		for(String item : parkInfo) {
			if(parkInfo.indexOf(item) % 2 == 0 || parkInfo.indexOf(item) == 0) {
				System.out.printf("%-20s", item);
			}else {
				System.out.println(item);
			}
		}
	}
	
	private void handleParks(String parkName) throws Exception {
		System.out.println("__________Park Information__________");
		System.out.println("---------------------------------------");
		listParkInfo(parkName);
		String choice = (String)menu.getChoiceFromOptions(PARK_MENU_OPTIONS);
		int parkId = parkDAO.getParkId(parkName);
		if(choice.equals(PARK_MENU_OPTION_VIEW)) {
			handleCampgrounds(parkId);
		} else if(choice.equals(PARK_MENU_OPTION_SEARCH)) {
			handleSearchReservation(parkId);
		} else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
			run();
		}
	}
	
	private void handleCampgrounds(int parkId) {
		List<Campground> campgrounds = campgroundDAO.getCampgroundInformation(parkId);
		String parkName = parkDAO.getParkName(parkId);
		System.out.println("____________________________________________________" + parkName + " Campgrounds___________________________________________________________________");
		System.out.printf("%-40s %-40s %-40s %-40s", "  Name", "  Open", "  Close", "  Daily Fee");
		System.out.println("\n-----------------------------------------------------------------------------------------------------------------------------------------");
		if(campgrounds.size() > 0) {
			int num = 1;
			for(Campground campground : campgrounds) {
				System.out.print(num + ") ");
				System.out.printf("%-40s %-40s %-40s %-40s", campground.getName(), campground.getOpenFrom(), campground.getOpenTo(), "$" + campground.getDailyFee());
				System.out.println();
				num += 1;
			}
		} else {
			System.out.println("\n ---No Results--- \n");
		}
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
	}
	
	private void handleSearchReservation(int parkId) throws Exception {
		handleCampgrounds(parkId);
		List<String> campground = campgroundDAO.getCampgroundId(parkId);
		System.out.println("Which campground? (enter 0 to cancel)");
		String choice = (String)menu.getChoiceWithoutDisplay(campground.toArray());
		if(Integer.valueOf(choice) == 0) {
			run();
		} 
		getDates(Integer.valueOf(choice));
	}
	
	private void getDates(int campgroundId) throws Exception {
		LocalDate today = LocalDate.now();
		LocalDate departureDate = null;
		LocalDate arrivalDate = null;
		long totalDays = 0;
		boolean done = false;
		while(!done) {
			while(!done) {
				System.out.println("What is the arrival date? (mm/dd/yyyy)");
				arrivalDate = menu.getDateFromUserInput();
				int arrivalMonth = arrivalDate.getMonthValue();
				if(arrivalDate.minus(Period.ofDays(1)).isBefore(today)) {
					System.out.println("Please enter a date after today's date");
				} else if(arrivalMonth >= campgroundDAO.getCampgroundOpenMonth(campgroundId) && arrivalMonth <= campgroundDAO.getCampgroundCloseMonth(campgroundId)) {
					done = true;
				} else {
					System.out.println("Please enter a date within the open season");
				}
			}
			done = false;
			while(!done) {
				System.out.println("What is the departure date?");
				departureDate = menu.getDateFromUserInput();
				int departureMonth = departureDate.getMonthValue();
				if(departureDate.minus(Period.ofDays(1)).isBefore(arrivalDate)) {
					System.out.println("Please enter a date at least one day after the arrival date");
				} else if(departureMonth <= campgroundDAO.getCampgroundCloseMonth(campgroundId)) {
					done = true;
				} else {
					System.out.println("Please enter a date within the open season");
				}
			}
			done = false;
			totalDays = ChronoUnit.DAYS.between(arrivalDate, departureDate);
			if(totalDays > 30) {
				System.out.println("Sorry, the maximum stay is limited to 30 days");
			} else {
				done = true;
			}
		}
		reserveSite(campgroundId, arrivalDate, departureDate, totalDays);
	}
	
	private void reserveSite(int campgroundId, LocalDate arrivalDate, LocalDate departureDate, long totalDays) throws Exception {	
		List<Integer> reservationIds = reservationDAO.getAvailableSites(campgroundId, arrivalDate, departureDate);
		BigDecimal campgroundFee = campgroundDAO.getCampgroundFee(campgroundId);
		BigDecimal totalCost = campgroundFee.multiply(new BigDecimal(totalDays));
		List<String> sites = new ArrayList<>();
		System.out.println("");
		printLn(1);
		for(int i = 0; i < reservationIds.size(); i++) {
			if((i) % 5 == 0 && i != 0) {
				String choice = (String)menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
				if(choice.equals(SITE_OPTION_RESERVE)) {
					makeReservation(sites.toArray(), totalCost, arrivalDate, departureDate);
				} else if(choice.equals(SITE_OPTION_NEXT)) {
					sites.removeAll(sites);
					printLn(1);
				} else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
					run();
				}
			} 
			Site site = siteDAO.getSiteInfo(reservationIds.get(i));
			String siteInfo = String.format("%-20s %-20s %-20s %-20s %-20s %-20s", site.getSiteNumber(), site.getMaxOccupancy(), convertBoolean(site.isAccessible()), site.getRvLength(), convertBoolean(site.isUtilities()), " $" + totalCost);
			System.out.println(siteInfo);
			sites.add(siteInfo);
			if (i == reservationIds.size() - 1) {
				String choice = (String)menu.getChoiceFromOptions(ALT_SITE_MENU_OPTIONS);
				if(choice.equals(SITE_OPTION_RESERVE)) {
					makeReservation(sites.toArray(), totalCost, arrivalDate, departureDate);
				} else if(choice.equals(MENU_OPTION_RETURN_TO_MAIN)) {
					run();
				}
			}
		}
	}
	
	private void makeReservation(Object[] sites, BigDecimal totalCost, LocalDate arrivalDate, LocalDate departureDate) {
		System.out.println("\nWhich site would you like to reserve? (enter number choice to left of Site no.)\n");
		printLn(1);
		String choice = (String)menu.getChoiceFromOptions(sites);
		System.out.println("What name should the reservation be made under?");
		String name = menu.getInfoFromUser();
		System.out.println("\n\nYour reservation is complete! Here is your receipt:\n--------------------------------------------------- ");
		System.out.println("Customer Name: " + name);
		Random confirmationNum = new Random();
		System.out.println("Confirmation number: " + confirmationNum.nextInt(9000000) + 1000000);
		System.out.println("Arrival Date: " + arrivalDate + "\nDeparture Date: " + departureDate + "\nCheck in: 3 p.m. \n\nSite reservation info:");
		printLn(0);
		System.out.println("\n" + choice);
		System.out.println("------------------------\nTotal Paid: $" + totalCost + "\n\n");
		System.exit(0);
	}
	
	public static String convertBoolean(boolean trueFalse) {
		return trueFalse ? "yes" : "no";
	}
	
	public void printLn(int num) {
		System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s", "Site No.", "Max Occupancy", "Accessible", "Max RV Length", "Utilities", "Total Cost");
		if(num == 1) {
			System.out.println("\n-----------------------------------------------------------------------------------------------------------------");
		}
	}
	
}
