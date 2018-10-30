package com.techelevator.campground.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.techelevator.campground.Campground;

public class Menu {
	
	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}
	
	public Object getChoiceWithoutDisplay(Object[] options) {
		Object choice = null;
		while(choice == null) {
			String userInput = in.nextLine();
			try {
				int selectedOption = Integer.valueOf(userInput);
				if(selectedOption == 0) {
					choice = "0";
				} else if(selectedOption <= options.length) {
					choice = options[selectedOption - 1];
				}
			} catch(NumberFormatException e) {
			}
			if(choice == null) {
				out.println("\n*** "+userInput+" is not a valid option ***\n");
			}
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}
//	
//	public Object getChoiceFromUserInput(List<Campground> campground) {
//		Object choice = null;
//		String userInput = in.nextLine();
//		int selectedOption = Integer.valueOf(userInput);
//		try {
//			if(selectedOption <= campground.size()) {
//				choice = campground.get(selectedOption - 1).getCampgroundId();
//			}
//		} catch(NumberFormatException e) {
//		}
//		if(choice == null) {
//			out.println("\n*** " + userInput + " is not a valid option ***\n");
//		}
//		return choice;
//	}
	
	public String getInfoFromUser() {
		String userInput = in.nextLine();
		return userInput;
	}
	
	public LocalDate getDateFromUserInput() {
		LocalDate inputDate = null;
		LocalDate outputDate = null;
		boolean done = false;
		while(!done) {
			done = true;
			try {
				String userInput = in.nextLine();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.ENGLISH);
				DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
				inputDate = LocalDate.parse(userInput, formatter);
				String convert = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH).format(inputDate);
				outputDate = LocalDate.parse(convert, formatter2);
			} catch(Exception e) {
				System.out.println("Please enter a date in this format: (mm/dd/yyyy)");
				done = false;
			}
		}
		return outputDate;
	}
	
	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose an option >>> \n");
		out.flush();
	}

}
