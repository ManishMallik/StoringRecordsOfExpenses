package ExpenseRecords;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

//This class shows JOptionPanes for user interactions, allowing the user to create accounts log in, and add, edit, delete, or check own expenses
public class Driver 
{
	
	private static UserExpenses ownExpenses;
	private static UserName listOfNames;
	
    /**
     * main initializes ownExpenses(an arrayList from the class UserExpenses that contains ExpenseRecords objects)
     * and listOfNames(an arrayList from the class UserName that contains User objects). It also runs the whole program
     * and loads the User objects from a file to listOfNames
     * @param args the command line arguments
     * @throws ClassNotFoundException 
     */
    public static void main(String[] args) throws ClassNotFoundException 
    {
        // TODO code application logic here
    	//ownExpenses is an arrayList from the UserExpenses class that will store ExpenseRecords objects
    	//listOfNames is an arrayList from the UserName class that will store User objects
    	//When the program runs, the listOfNames will load all User objects from a binary file 1st
    	ownExpenses = new UserExpenses();
        listOfNames = new UserName();
        loadUsers();
        runWholeProgram();
    }
    
    /**
     * runWholeProgram gives the beginning screen. The user gets to log in, create an account, or end the program
     */
    public static void runWholeProgram()
    {
    	/* The user has 3 buttons to click on: Log In, Create an Account, and Close Out. 
    	 * If the user closes out, the User objects in listOfNames will be saved in a file.
    	 */
    	Object[] choices = {"Log In", "Create an Account", "Close Out"};
    	int option = JOptionPane.showOptionDialog(null, "Menu", "Log in or create your account", 
    			JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
    	if(option != JOptionPane.CLOSED_OPTION && option != 2)
    		logInOption(option);
    	else
    		saveUsers();
    }
    
    /**
     * logInOption is where the user chooses to either log in or create an account if he/she chose not to end the program
     * @param choice the button that the user clicked on. "Log In" button is choice 0, and "Create an Account" is 
     * choice 1
     */
    public static void logInOption(int choice)
    {
    	/*
    	 * When typing in JTextFields, whatever is typed in there can be shown. People do not want others to know their
    	 * own passwords, so JPasswordField is used to cover up the password as black dots. The name of the user,
    	 * the username, and password are needed to create a User object.
    	 */
    	JTextField field1 = new JTextField();
    	JTextField field2 = new JTextField();
    	JPasswordField field3 = new JPasswordField();
    	String nameOfUser = "";
    	String ownUserName = "";
    	String ownPassword = "";
    	int input = 0;
    	User account = new User(nameOfUser, ownUserName, ownPassword);
    	
    	/*
    	 * If the user is only logging in, the username field and the password field are only needed.
    	 * If the user is creating an account, the name field, username field, and password field are needed
    	 * so that a username can be assigned to a name.
    	 */
    	if(choice == 0)
    		loggingIn(account, field2, field3, nameOfUser, ownUserName, ownPassword, input);
    	else 
    		creatingNewAccounts(account, field1, field2, field3, nameOfUser, ownUserName, ownPassword, input);
    }
    
    /**
     * loggingIn shows a username and password fields for the user to enter before the user will continue on to access
     * his/her expense records 
     * @param temporary an empty User object(it had to be instantiated in the previous method so that it could be passed
     * on as a parameter) that will later only have an empty name but a complete username and password
     * @param temp1 the username text field
     * @param temp2 the password field. Password will be shown as black dots
     * @param name the name of the user. It will be empty in this case. The program will try to get the name of the user
     * if the user successfully enters the correct username and password
     * @param username the username from the username text field after the user chooses to continue on
     * @param password the password from the password field after the user submits
     * @param input the choices the user makes(which button the user clicks on)
     */
    public static void loggingIn(User temporary, JTextField temp1, JPasswordField temp2, String name, String username, String password, 
    		int input)
    {
    		/*
    		 * When the user is logging in, these are the labels and text fields that will show up to tell the user what
    		 * to do
    		 */
    		Object[] loggingIn =
			{
				"Enter your username:", temp1,
				"Enter your password:", temp2,
			};
			
    		/*
    		 * These are the buttons the user will get if the user fails to enter a correct username or password
    		 */
			Object[] options = {"Try again", "Create an account"};
			
			/*
			 * If the user clicks on the OK button, the username and password will get what was typed in the text fields
			 * by the user. If the user clicks on cancel, he/she will go back to the home page
			 */
			int logInOrNot = JOptionPane.showConfirmDialog(null, loggingIn, "Log in to your account", JOptionPane.OK_CANCEL_OPTION);
			if(logInOrNot == JOptionPane.OK_OPTION)
			{
				//The username and password fields cannot be empty if the user wants to log in
				while((temp1.getText().isBlank() ||new String(temp2.getPassword()).equals("")) && logInOrNot == JOptionPane.OK_OPTION)
					logInOrNot = JOptionPane.showConfirmDialog(null, loggingIn, "Please fill in every field", JOptionPane.OK_CANCEL_OPTION);
				if(logInOrNot != JOptionPane.OK_OPTION)
					runWholeProgram();
				else
				{
					/*
					 * The name is empty because the program will go ahead and get the name of the user by
					 * doing passwordMatch with User temporary consisting of an empty name. If the username exists and the
					 * password assigned to it matches with the password the user inputs, the program will load the user's expense
					 * records containing the user's username and allow him to manage them. If the user enters an invalid username or password, 
					 * he/she gets the option to retry or create a new account. He/she goes back to the main menu if clicking on the X
					 * button.
					 */
					name = "";
					username = temp1.getText();
					password = new String(temp2.getPassword());
					temporary = new User(name, username, password);
					if(listOfNames.passwordMatch(temporary))
					{
						loadExpenses(username);
						managingRecords(temporary);
					}
					else
					{
						input = JOptionPane.showOptionDialog(null, "Wrong username or password. Try again or create an account", 
							"Failed to log in", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
						if(input == JOptionPane.CLOSED_OPTION)
							runWholeProgram();
						else
							logInOption(input);
					}
				}
			}
			else
				runWholeProgram();
    }
    
    /**
     * creatingNewAccounts allows a user to create a new account so that he/she can have his/her own expense records
     * stored in separate files
     * @param temporary an empty User object that will later have a User object with an actual name, username,
     * and password
     * @param temp1 the text field where the user enters his/her real name
     * @param temp2 the text field where the user creates a username
     * @param temp3 the password field where the user creates a password that cannot be seen
     * @param name the name of the user that the program will store after this method
     * @param username the user's username that will be created and stored after this method
     * @param password the user's password that will be created and stored after this method
     * @param input the choices the user makes(which button the user clicks on)
     */
    public static void creatingNewAccounts(User temporary, JTextField temp1, JTextField temp2, JPasswordField temp3, String name, 
    		String username, String password, int input)
    {
    		/*
    	 	* These are the labels that will show up when the user is creating a new account. There will be sections to
    	 	* enter the user's name, create a username, and create a password
    	 	*/
    		Object[] account = 
			{
				"Enter your name:", temp1,
				"Create your username:", temp2,
				"Create your password:", temp3,
			};
			/*
			 * If the user fails to create a new account due to an existing username or entering a username with a space,
			 * he/she will get to choose to either log in, try again in creating an account, or go to main menu
			 */
			Object[] choices = {"Log In", "Create an Account", "Main Menu"};
			
			/*
			 * If the user clicks "OK", the name, username, and password variables will get the text from their
			 * appropriate fields, and User object will be created to compare with the other objects in listOfNames.
			 * If the user clicks "Cancel", the beginning screen will show up, and nothing will be saved.
			 */
			int option = JOptionPane.showConfirmDialog(null, account, "Create an account", 
					JOptionPane.OK_CANCEL_OPTION);
			if(option == JOptionPane.OK_OPTION)
			{
				//The fields for name, username, and password cannot be empty if the user intends to create an account
				while((temp1.getText().isBlank() || temp2.getText().isBlank() || 
						new String(temp3.getPassword()).equals("")) && option == JOptionPane.OK_OPTION)
					option = JOptionPane.showConfirmDialog(null, account, "Please fill in every field", 
							JOptionPane.OK_CANCEL_OPTION);
				if(option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION)
					runWholeProgram();
				else 
				{
					name = temp1.getText();
					username = temp2.getText();
					password = new String(temp3.getPassword());
					temporary = new User(name, username, password);
					/*
				 	* If the account/User object that the user attempts to create has the same username as a username from
				 	* any of the User objects in listOfNames or if the username that the user types in has a space, an error
				 	* message will pop up, saying if the username already exists or if the username has a space and give the
				 	* user an option to try again or log in or go to main menu. If the user's new username does not contain
				 	* spaces and does not exist, the user's new account/User object will be added to listOfNames, and a
				 	* message will tell that the account is created and the user should memorize his/her username and
				 	* password.
				 	*/
					if(!listOfNames.userNameExists(temporary) && !username.contains(" "))
					{
						listOfNames.addAccount(temporary);
						createOwnRecords(username);
						JOptionPane.showMessageDialog(null, "Account is created. Please memorize your username and password.");
						runWholeProgram();
					}
					else
					{
						if(username.contains(" "))
							input = JOptionPane.showOptionDialog(null, "Usernames cannot contain spaces. Please create a different username, "
								+ "log in, or go to main menu", "Try again", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, 
								choices, choices[0]);
						else
							input = JOptionPane.showOptionDialog(null, "This username already exists. Please create a different username, "
								+ "log in, or go to main menu", "Try again", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, 
								choices, choices[0]);
						if(input == JOptionPane.CLOSED_OPTION || input == 2)
							runWholeProgram();
						else
						logInOption(input);
					}
				}
			}
			else
			{
				runWholeProgram();
			}
    }
    
    /**
     * managingRecords will show a JOptionPane with choices on how the user can manage his/her expense records. The user
     * also has a choice of logging out
     * @param temporary the account/User object that allows the program to get the name of the user based on the
     * location of his/her username entered
     */
    public static void managingRecords(User temporary)
    {
    	/*
    	 * The name of the user will be received by the program by getting the user's username's location in listOfNames.
    	 * The name will be mentioned on the JOptionPane.
    	 * options consist of the choices for the user to manage his/her expense records or log out.
    	 * Entering expenses would be input 0, checking them would be input 1, and editing/deleting them would be
    	 * input 2. If the user logs out, ownExpenses will be cleared, and the log in JOptionPane will be shown.
    	 */
    	String name = listOfNames.getName(listOfNames.userNameLocation(temporary));
    	String username = temporary.getUsername();
    	Object[] options = {"Enter expenses into your records", "Check your expense records", "Edit or Delete", "Log Out"};
    	int input = JOptionPane.showOptionDialog(null, "Hello " + name + ". Click on a button to do the following action.", 
    			"Your Account", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    		switch(input)
    		{
    			case 0:
    				enterExpenses(temporary, username);
    				break;
    			case 1:
    				checkExpenses(temporary);
    				break;
    			case 2:
    				editOrDeleteExpenses(temporary, username);
    				break;
    			default:
    				JOptionPane.showMessageDialog(null, "You have successfully logged out.");
    				ownExpenses.clearList();
    				logInOption(0);
    				break;
    		}
    }
    
    /**
     * enterExpenses allows a user to enter new ExpenseRecords day by day.
     * @param temporary the account/User object of the user
     * @param user the username of the user
     */
    public static void enterExpenses(User temporary, String user)
    {
    	/*
    	 * There will be a dropdown menu of expense types. There will be text fields where the user enters a company or
    	 * store name he/she went to and the amount of money they spent on that type of expense. There is a text field
    	 * for expense type that will be displayed if the user chooses other as an expense type. ExpenseRecords newEntry
    	 * is declared and will later be instantiated
    	 */
    	String[] expenseChoices = {"Marketing, Advertising, and Promotion","Rent and Insurance", "Deprication", "Interest",
    			"Taxes", "Impairment Charges", "Education", "Groceries", "Healthcare","Other"};
    	String date = "";
    	String companyName = "";
    	String expenseType = "";
    	double expenseAmount = 0.0;
    	JTextField dateInput = new JTextField();
    	JTextField compName = new JTextField();
    	JTextField expType = new JTextField();
    	JTextField expAmt = new JTextField();
    	boolean validDate = false;
    	ExpenseRecords newEntry;
    	int option = 0;
    	
    	//dropdown is shown
    	expenseType = (String)JOptionPane.showInputDialog(null, "Choose the type of Expense", "Expense Types", 
    			JOptionPane.QUESTION_MESSAGE, null, expenseChoices, expenseChoices[0]);
    	//expenseType will be null if the user clicks "Cancel" or "X". This will take them back to the expenses menu.
    	if(expenseType == null)
    		managingRecords(temporary);
    	else
    	{
    		/*
    		 * If the user chooses "Other" as an expense type from the dropdown menu, he/she will have to type in an
    		 * expense type that was not in the drop down. The user also has to type in the company name and the amount
    		 * of money spent on the type of expense.
    		 */
    		if(expenseType.equals("Other"))
    		{
    			Object[] record = 
    				{
    					"Enter the date you paid on in mm/dd/yyyy:", dateInput,
    					"Enter the company or store you went to:", compName,
    					"Enter the expense type that you paid for:", expType,
    					"Enter how much money you spent for that type of expense(positive value, leave off dollar sign)", expAmt,
    				};
    			/*
    			 * The text fields for date, expenseType, companyName, and expenseAmount should be filled out before
    			 * continuing on, and whatever is typed in the text field for expenseAmount should be a valid number.
    			 * Another way to exit the while loop is to either click on the Cancel or X button, which takes the
    			 * user back to main menu
    			 */
    	    	while((expenseAmount <= 0.0 || companyName.isBlank() || expenseType.isBlank() || !date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") || 
    	    			!validDate) && option != JOptionPane.CANCEL_OPTION && option != JOptionPane.CLOSED_OPTION)
    	    	{
    	    		option = JOptionPane.showConfirmDialog(null, record, "Enter your expense record. Please fill in everything.", 
    	    				JOptionPane.OK_CANCEL_OPTION);
    				if(option == JOptionPane.OK_OPTION)
    				{
    					date = dateInput.getText();
    					companyName = compName.getText();
    					expenseType = expType.getText();
    					if(date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d"))
    					{
    						validDate = validateDate(date);
    						if(!validDate)
    							JOptionPane.showMessageDialog(null, "Please input a valid date");
    					}
    					try
    					{
    						expenseAmount = Double.parseDouble(expAmt.getText());
    					}
    					catch(NumberFormatException n)
    					{
    						JOptionPane.showMessageDialog(null, "You entered an invalid amount. Please try again.");
    					}
    				}	
    	    	}
    		}
    		/*If the user did not choose "Other" as an expense type, he/she will only have to fill out the company name
    		 * and the amount of money spent for the chosen expense type.
    		 */
    		else
    		{
    			Object[] record2 =
    			{
    				"Enter the date you paid on in mm/dd/yyyy:", dateInput,
    				"Enter the company or store you went to:", compName,
    				"Enter how much money you spent for that type of expense(positive value, leave off dollar sign)", expAmt,
    			};
    			/*
    			 * text fields for date, expenseAmount, and companyName should not be empty if the user wants to enter an
    			 * expense record. Also the text field for expenseAmount should contain a valid decimal number.
    			 * The user can click on Cancel or X to go back to the dropdown menu of expense types
    			 */
    			while((expenseAmount <= 0.0 || companyName.isBlank() || !date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") || !validDate) && 
    					option != JOptionPane.CANCEL_OPTION && option != JOptionPane.CLOSED_OPTION)
    	    	{
    				option = JOptionPane.showConfirmDialog(null, record2, "Enter your expense record. Please fill in every section.", 
    						JOptionPane.OK_CANCEL_OPTION);
    				if(option == JOptionPane.OK_OPTION)
    				{
    					date = dateInput.getText();
    					companyName = compName.getText();
    					if(date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d"))
    					{
    						validDate = validateDate(date);
    						if(!validDate)
    							JOptionPane.showMessageDialog(null, "Please input a valid date");
    					}
    					try
    					{
    						expenseAmount = Double.parseDouble(expAmt.getText());
    					}
    					catch(NumberFormatException n)
    					{
    						JOptionPane.showMessageDialog(null, "You entered an invalid amount. Please try again.");
    					}
    				}
    	    	}
    		}
    		/*
    		 * If the user clicks on OK, an ExpenseRecords object will be created with the date, companyName, expenseType,
    		 * and expenseAmount. They will not be empty values since the program will check if they are filled out.
    		 * After the ExpenseRecords object is made, it gets added to ownExpenses and it will be saved in the user's
    		 * own file of expense records. A message will say that it is added to the user's expense records, and the
    		 * user will go to menu of managing expense records.
    		 */
    		if(option == JOptionPane.OK_OPTION)
    		{
    			newEntry = new ExpenseRecords(date, companyName, expenseType, expenseAmount);
    			ownExpenses.addExpenseRecord(newEntry);
    			saveExpenses(user);
    			//System.out.println(ownExpenses);
    			JOptionPane.showMessageDialog(null, "This has been added to your expense records");
    			managingRecords(temporary);
    		}
    		/*
    		 * If the user clicks on Cancel or X when entering company name, expense amount, and possibly expense type,
    		 * he/she will go back to the dropdown menu.
    		 */
    		else
    		{
    			enterExpenses(temporary, user);
    		}
    	}
    }
    
    /**
     * checkExpenses allows the user to check his expense records daily or monthly
     * @param temp the account/User object that the user is using
     */
    public static void checkExpenses(User temp)
    {
    	/*
    	 * There will be an option at the end if the user wants to stop checking their expenses or not. There will
    	 * be a yes button and a no button.
    	 */
    	boolean stop = false;
    	Object[] options = 
    	{
    		"Yes",
    		"No",
    	};
    	/*
    	 * There will be a Daily Expenses button and a Monthly Expenses button that will show up first. This is where the
    	 * user will choose how to look at their expense records.
    	 */
    	Object[] expenses =
   		{
   			"Daily Expenses",
   			"Monthly Expenses",
   		};
    	int input;
    	int n = JOptionPane.NO_OPTION;
    	/* If the user wants to stop checking their expenses, the user has to click on Yes and stop should become true
    	 */
    	while(!stop || n != JOptionPane.YES_OPTION) 
    	{
    		//field1 is for the date input, and field2 is for the month input
    		JTextField field1 = new JTextField();
    		JTextField field2 = new JTextField();
    		input = JOptionPane.showOptionDialog(null, "Please choose how you want to check your expenses.", "Checking Expense Records", 
    				JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, expenses, expenses[0]);    		
    		//If the user chooses Daily Expenses, the user will input a date in the mm/dd/yyyy format. 
    			if(input == 0)
    			{
    				Object[] entry = {"Enter a date in the format of mm/dd/yyyy", field1};
    				String dateInput = "";
    				int choice = JOptionPane.showConfirmDialog(null, entry, "Checking Daily Expenses", JOptionPane.OK_CANCEL_OPTION);
    				/*
    				 * The process of accessing daily expenses continues if the user clicks on OK. Otherwise the user will
    				 * go back to the Daily/Monthly Expenses options.
    				 */
    				if(choice == JOptionPane.OK_OPTION)
    				{
    					dateInput = field1.getText();
    					/*
    					 * If the date input is invalid, the user will have to go through this while loop unless he/she
    					 * enters a date in the valid format or the user clicks on Cancel or X
    					 */
    					while(!dateInput.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") && choice != JOptionPane.CANCEL_OPTION && 
    							choice != JOptionPane.CLOSED_OPTION)
    					{
    						choice = JOptionPane.showConfirmDialog(null, entry, "Invalid input. Please input the date in this format: "
    								+ "mm/dd/yyyy", JOptionPane.OK_CANCEL_OPTION);
    						if(choice == JOptionPane.OK_OPTION)
    							dateInput = field1.getText();
    					}
    					//If user clicks on Cancel or X, the user will go back to the Daily/Monthly Expenses options
    					if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION) 
    						stop = true;
    					/*
    					 * If the user has entered a valid date format and clicks on OK, the expense records on the given
    					 * date will be shown. It states the companies, the expense types, and the amount spent for each
    					 * of those expense types. The program will then give an option for the user to stop checking
    					 * expenses or no. If the user chooses yes, the user will go back to the menu of choosing how to
    					 * manage the expense records.
    					 */
    					else
    					{
    						dateInput = field1.getText();
    						JOptionPane.showMessageDialog(null, ownExpenses.dailyExpenses(dateInput));
    						n = JOptionPane.showOptionDialog(null, "Do you want to stop checking?", "Do you want to continue checking?", 
    								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    			    		if(n == JOptionPane.YES_OPTION)
    			    			stop = true;
    					}
    				}
    				else
    					stop = true;
    			}
    			//If the user chooses Monthly Expenses, the user will input the month in the format mm/yyyy
    			else if(input == 1)
    			{
    				Object[] entry2 = {"Enter a month in the format of mm/yyyy", field2};
    				String monthInput = "";
    				int choice = JOptionPane.showConfirmDialog(null, entry2, "Checking monthly expenses", JOptionPane.OK_CANCEL_OPTION);
    				/*
    				 * The process of accessing monthly expenses continues if the user clicks on OK. Otherwise the user 
    				 * will go back to the Daily/Monthly Expenses options.
    				 */
    				if(choice == JOptionPane.OK_OPTION)
    				{
    					monthInput = field2.getText();
    					/*
    					 * If the user enters an invalid formatted month, the user will have to go through this while loop
    					 * until the user enters a month with the valid format or clicks on Cancel/X.
    					 */
    					while(!monthInput.matches("\\d\\d/\\d\\d\\d\\d") && choice != JOptionPane.CANCEL_OPTION && 
    							choice != JOptionPane.CLOSED_OPTION)
    					{
    						choice = JOptionPane.showConfirmDialog(null, entry2, "Invalid input. Please input the month in this format: mm/yyyy",
    								JOptionPane.OK_CANCEL_OPTION);
    						if(choice == JOptionPane.OK_OPTION)
    							monthInput = field2.getText();
    					}
    					//If the user clicks on "Cancel"/"X", the user will go back to Daily/Monthly Expenses options
    					if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
    						stop = true;
    					/*
    					 * If the user has entered the correct format for the month and clicks on OK, the user will get
    					 * to see the expense records on the same month. The dates, companies, expense types, and expense
    					 * amounts will be listed, and the total amount of money spent on that month will be shown. Then,
    					 * the program will tell the user whether or not to stop checking expenses. The user will go back
    					 * to the menu for managing records by clicking Yes.
    					 */
    					else
    					{
    						monthInput = field2.getText();
    						JOptionPane.showMessageDialog(null, ownExpenses.monthlyExpenses(monthInput));
    						n = JOptionPane.showOptionDialog(null, "Do you want to stop checking?", "Do you want to continue checking?", 
    								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
    			    		if(n == JOptionPane.YES_OPTION)
    			    			stop = true;
    					}
    				}
    				else 
    					stop = true;
    			}
    			//If the user clicks on X when on the Daily/Monthly Expenses, he/she will go to menu for managing records
    			else
    			{
					stop = true;
					n = JOptionPane.YES_OPTION;
				}
    	}
    	managingRecords(temp);
    }
    
    /**
     * editOrDeleteExpenses allows the user to modify their expense records by editing or deleting them
     * @param temporary the account/User object the user is signed into
     * @param name the username of the user
     */
    public static void editOrDeleteExpenses(User temporary, String name)
    {
    	/*
    	 * An arrayList from UserExpenses called edit will store ExpenseRecords objects with the same date. The date will
    	 * be entered by the user. field1 is for entering the date, and field2 is entering a number to choose which object
    	 * will be edited or deleted.
    	 */
    	UserExpenses edit = new UserExpenses();
    	JTextField field1 = new JTextField();
    	JTextField field2 = new JTextField();
    	ExpenseRecords chosenRecord;
    	//There will be 2 buttons called Edit and Delete
    	Object[] choices =
       	{
       		"Edit",
       		"Delete",
       	};
    	Object[] entry = {"Enter a date in this format: mm/dd/yyyy", field1};
    	//There will be a dropdown of expenseTypes for the user to choose when modifying an expense record
    	String[] expenseChoices = {"Marketing, Advertising, and Promotion", "Rent and Insurance", "Deprication", "Interest", 
    			"Taxes", "Impairment Charges", "Education", "Groceries", "Healthcare","Other"};
    	int choice;
    	int size = 0;
    	int convertedNumber = 0;
    	String dateInput = "";
    	String input = "";
    	String date = "";
    	String company = "";
    	String expenseType = "";
    	double expenseAmount = 0.0;
    	boolean validDate = false;
    	//Date gets entered
    	choice = JOptionPane.showConfirmDialog(null, entry, "Enter a date", JOptionPane.OK_CANCEL_OPTION);
    	//User will go to menu of managing records if choosing Cancel or X
    	if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
    		managingRecords(temporary);
    	//Process continues if user clicks on OK
    	else
    	{
    		//checks if the dateInput format is correct or not
    		dateInput = field1.getText();
			//If the date is in wrong format, the user must enter the date in correct format or click on Cancel/X
    		while(!dateInput.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") && choice != JOptionPane.CANCEL_OPTION && choice != JOptionPane.CLOSED_OPTION)
			{
				choice = JOptionPane.showConfirmDialog(null, entry, "Invalid input. Please input the date in this format: mm/dd/yyyy", 
						JOptionPane.OK_CANCEL_OPTION);
				if(choice == JOptionPane.OK_OPTION)
					dateInput = field1.getText();
			}
			//User goes back to menu of managing records if he/she clicks on Cancel/X
			if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
	    		managingRecords(temporary);
			//Process continues if user clicks on OK
			else
			{
				//if the date does exist in the array list. The else is at line 794
				if(ownExpenses.dailyExpenses(dateInput) != "You either have never paid for anything on this date or you have entered"
						+ " an invalid date.")
				{
					//Program will get all ExpenseRecords objects with the user's date input stored in list edit
					edit.editOrDeleteRecords(dateInput, ownExpenses.dailyExpenses(dateInput));
					//System.out.println(edit);
					size = edit.size();
					//The next JOptionPane will show the contents of each ExpenseRecords object in toString format
					Object[] entry2 = {"These are the expenses you paid for on the date you entered.\n" + edit + 
		    			"\nEnter a number to either edit or delete.", field2};
					//choose a number
					choice = JOptionPane.showConfirmDialog(null, entry2 ,"Enter a number", JOptionPane.OK_CANCEL_OPTION);
					//User must enter a number choice that is shown on the JOptionPane
					//User can also exit while loop by clicking on Cancel/X
					while(!field2.getText().matches("[1-" + size + "]") && choice != JOptionPane.CANCEL_OPTION && 
							choice != JOptionPane.CLOSED_OPTION)
					{
						choice = JOptionPane.showConfirmDialog(null,entry2,"Wrong Input. Please try again.", JOptionPane.OK_CANCEL_OPTION);
					}
					//User goes back to menu of managing records if he/she clicks on Cancel/X
					if(choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.CLOSED_OPTION)
						managingRecords(temporary);
					else
					{
						input = field2.getText();
						//convertedNumber is 1 ahead of the index because the first expense record listed is assigned to 1
						convertedNumber = Integer.parseInt(input);
						/*
						 * To get the record the user chose by entering a number, the convertedNumber must be subtracted
						 * by 1 before being entered as an index
						 */
						chosenRecord = edit.get(convertedNumber - 1);
						//System.out.println(chosenRecord);
						//System.out.println(edit.indexOf(chosenRecord));
						//System.out.println(edit.get(edit.indexOf(chosenRecord)));
						//System.out.println(ownExpenses.indexOf(chosenRecord));
						//User gets the choice to edit or delete the expense record chosen
						choice = JOptionPane.showOptionDialog(null, "Choose to either edit or delete this", "Choices", 
								JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
						//edit: choice 0. delete: choice 1.
						if(choice == 0)
						{
							//Similar process as the method enterExpenses up till line 764
							JTextField dateFromUser = new JTextField();
							JTextField compName = new JTextField();
							JTextField expType = new JTextField();
							JTextField expAmt = new JTextField();
							expenseType = chosenRecord.getExpenseType();
							expenseType = (String)JOptionPane.showInputDialog(null, "Your Expense type earlier was this: " + expenseType + 
									"\nChange your expenseType", "Expense Types", JOptionPane.QUESTION_MESSAGE, null, expenseChoices, 
									expenseChoices[0]);
							if(expenseType == null)
								managingRecords(temporary);
							else
							{
								date = chosenRecord.getDate();
								company = chosenRecord.getCompany();
								expenseAmount = chosenRecord.getExpenseAmt();
								if(expenseType.equals("Other"))
								{
									expenseType = chosenRecord.getExpenseType();
									Object[] record = 
									{
										"You earlier listed this date: " + date + "\nEnter a new date or the same one in mm/dd/yyyy: ", 
										dateFromUser,
										"You earlier had this company listed: " + company + "\nEnter the company or store you went to:", 
										compName,
			    						"You earlier had this expense listed: " + expenseType + "\nEnter the expense type that you paid for:", 
			    						expType,
			    						"You earlier had this much money spent listed: " + String.format("%.2f",expenseAmount) + 
			    						"\nEnter how much money you spent for that type of expense(positive value, leave off dollar sign)", 
			    						expAmt,
									};
									company = "";
									expenseType = "";
									expenseAmount = 0.0;
									while((expenseAmount <= 0.0 || company.isBlank() || expenseType.isBlank() || 
											!date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") || !validDate) && choice != JOptionPane.CANCEL_OPTION && 
											choice != JOptionPane.CLOSED_OPTION)
									{
										choice = JOptionPane.showConfirmDialog(null, record, 
												"Enter your expense record. Please fill in everything.", JOptionPane.OK_CANCEL_OPTION);
										if(choice == JOptionPane.OK_OPTION)
										{
											date = dateFromUser.getText();
											company = compName.getText();
											expenseType = expType.getText();
											if(date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d"))
					    					{
												validDate = validateDate(date);
												if(!validDate)
					    							JOptionPane.showMessageDialog(null, "Please input a valid date");
					    					}
											try
											{
												expenseAmount = Double.parseDouble(expAmt.getText());
											}
											catch(NumberFormatException n)
											{
												JOptionPane.showMessageDialog(null, "You entered an invalid amount. Please try again.");
											}
										}	
									}
								}
								else
								{
									Object[] record2 =
									{
										"You earlier listed this date: " + date + "\nEnter a new date or the same one in mm/dd/yyyy: ", 
										dateFromUser,
										"You earlier had this company listed: " + company + "\nEnter the company or store you went to:", 
										compName,
										"You earlier had this much money spent listed: " + String.format("%.2f",expenseAmount) + 
										"\nEnter how much money you spent for that type of expense(positive value, leave off dollar sign)", 
										expAmt,
									};
									company = "";
									expenseAmount = 0.0;
									while((expenseAmount <= 0.0 || company.isBlank() || !date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d") || 
											!validDate) && choice != JOptionPane.CANCEL_OPTION && choice != JOptionPane.CLOSED_OPTION)
									{
										choice = JOptionPane.showConfirmDialog(null, record2, 
												"Enter your expense record. Please fill in every section.", JOptionPane.OK_CANCEL_OPTION);
										if(choice == JOptionPane.OK_OPTION)
										{
											date = dateFromUser.getText();
											company = compName.getText();
											if(date.matches("\\d\\d/\\d\\d/\\d\\d\\d\\d"))
					    					{
					    						validDate = validateDate(date);
					    						if(!validDate)
					    							JOptionPane.showMessageDialog(null, "Please input a valid date");
					    					}
											try
											{
												expenseAmount = Double.parseDouble(expAmt.getText());
											}	
											catch(NumberFormatException n)
											{
												JOptionPane.showMessageDialog(null, "You entered an invalid amount. Please try again.");
											}
										}
									}
								}
								/*
								 * If the user chooses OK, the modified expense record will replace the chosenRecord
								 * in ownExpenses, and the user's expense records will be saved in his/her file.
								 * A message will show that the edited record has been saved, and the user will go to menu
								 * of managing records.
								 */
								if(choice == JOptionPane.OK_OPTION)
								{
									//System.out.println(ownExpenses.indexOf(chosenRecord));
									ownExpenses.set(ownExpenses.indexOf(chosenRecord), new ExpenseRecords(date, company, expenseType, 
											expenseAmount));
									saveExpenses(name);
									JOptionPane.showMessageDialog(null, "This record has been edited and saved to your expense records");
									managingRecords(temporary);
								}
								//Nothing will be saved, and user will go back to menu of managing records.
								else
									managingRecords(temporary);
							}	
						}
						//delete the chosen record from ownExpenses, which is the original arrayList of ExpenseRecords
						else if(choice == 1)
						{
							chosenRecord = edit.get(convertedNumber - 1);
							ownExpenses.remove(chosenRecord);
							saveExpenses(name);
							JOptionPane.showMessageDialog(null, "This record has been successfully removed");
							managingRecords(temporary);
						}
						//If user clicks on X when asked to edit or delete, user will go to menu of managing records.
						else
						{
							managingRecords(temporary);
						}
					}
				}
				else
				{
					/*
					 * If the date that the user enters is not in ownExpenses, the program will say that nothing was paid
					 * on that date, and the user will go back to the menu of managing records.
					 */
					JOptionPane.showMessageDialog(null, "You did not pay for anything on this date");
					managingRecords(temporary);
				}
			}
    	}
    }
    
    /**
     * saveUsers saves the accounts/User objects from listOfNames to a binary file
     */
    public static void saveUsers()
    {
    	//ObjectOutputStream writes every User object from listOfNames to a binary file
    	//The binary file will mix up the order of how a User object will be printed
    	try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("userAccounts.dat")))
		{
			output.writeObject(listOfNames);
			output.close();
		}
		catch(IOException e)
		{
			//System.out.println (e);
		}
    }
    
    /**
     * loadUsers loads the accounts/User objects from a binary file to listOfNames
     */
    public static void loadUsers()
    {
    		//ObjectInputStream reads in every User object from the binary file and storing them into listOfNames
	   		try(ObjectInputStream input = new ObjectInputStream(new FileInputStream("userAccounts.dat")))
		  	{
		  		listOfNames = (UserName)input.readObject();
		   		//System.out.println(listOfNames);
		   	}
		  	catch (IOException | ClassNotFoundException e)
		   	{
		  		//System.out.println ("Failed to read input " + e);
		   	}
    }
    
    /**
     * createOwnRecords creates a file for a user after the user creates a new account/User object
     * @param name the username of the user
     */
    public static void createOwnRecords(String name)
    {
    	//A file of expense records will consist of a user's username. It will be created after the user creates a new account
    	//Name of user is not used becaused two users can have the same name, but they cannot have same username
    	File ownRecordList = new File(name + "Records.txt");
    	try 
    	{
    		ownRecordList.createNewFile();
    		//if(ownRecordList.createNewFile())
    			//System.out.println("File is created");
    		//else
    			//System.out.println("File isnt created");
    	}
    	catch(IOException e)
    	{
    		//System.out.println(e);
    	}
    }
    
    /**
     * saveExpenses saves the expense records in a certain user's file by reading the file from ownExpenses to the text
     * file that includes the user's name
     * @param name the username of the user
     */
    public static void saveExpenses(String name)
    {
    	/*
    	 * PrintWriter will write expense records(in saveToFile format) in the file of the username of the logged in user and 
    	 * save them.
    	 */
    	try
		{
			PrintWriter userExpenses = new PrintWriter(new File(name + "Records.txt"));
			ownExpenses.sort();
			userExpenses.print(ownExpenses.saveToFile());
			userExpenses.close();
		}
		catch(IOException e)
		{
			//System.out.println (e);
		}
    }
    
    /**
     * loadExpenses reads a certain user's file of expense records and stores them in ownExpenses
     * @param name the username of the user
     */
    public static void loadExpenses(String name)
    {
    	/*
    	 * A scanner will read in the file of a logged in user, reading in each component of the ExpenseRecords object,
    	 * creating an ExpenseRecords object, and adding to to ownExpenses, and so on until there are no more lines.
    	 */
    	Scanner saved;
   		try
   		{
   			saved = new Scanner(new File(name + "Records.txt"));
   			while(saved.hasNext())
   			{
   				String date = saved.nextLine();
   				String companyName = saved.nextLine();
   				String expense = saved.nextLine();
   				double money = saved.nextDouble();
   				saved.nextLine();
   				ownExpenses.addExpenseRecord(new ExpenseRecords(date, companyName, expense, money));
   			}
   			//System.out.println(ownExpenses);
   		}
   		catch (IOException e)
   		{
   			//System.out.println ("Failed to read input " + e);
   		}
    }
    
    /**
     * validateDate checks if a user's entered date exists and is reasonable
     * @param date the date the user enters
     * @return true if the date exists or false if it does not
     */
    public static boolean validateDate(String date)
    {
    	//initialize validDate to false
    	boolean validDate = false;
    	String[] dateComponents = date.split("/");
    	//The number before the 1st slash is the month
		int month = Integer.parseInt(dateComponents[0]);
		//The number between the 2 slashes is the day
		int day = Integer.parseInt(dateComponents[1]);
		//The number after the last slash is the year
		int year = Integer.parseInt(dateComponents[2]);
		//2100 can be possible max as better apps may come up. 2020 is minimum
		if(year >= 2020 && year <= 2100)
			//If February is the month
			if(month == 2)
				//If it is a leap year, there should be 29 days or less
				if(year % 4 == 0)
				{
					if(day <= 29 && day > 0)
						validDate = true;
				}
				//If it is a normal year, there should be 28 days or less
				else
				{
					if(day <= 28 && day > 0)
						validDate = true;
				}
			//If the month is January, March, May, July, August, October, or December, there should be 31 days or less
			else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || 
					month == 12)
			{
				if(day <= 31 && day > 0)
					validDate = true;
			}
			//If the month is April, June, September, or November, there should be 30 days or less
			else if(month == 4 || month == 6 || month == 9 || month == 11)
				if(day <= 30 && day > 0)
					validDate = true;
		return validDate;
    }
}