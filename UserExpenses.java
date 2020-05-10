package ExpenseRecords;

import java.io.*;
import java.util.*;
//This is a list class that will manage ExpenseRecords objects. The user's actions will invoke certain methods to manage them
public class UserExpenses 
{
        
    /**
     * Creates a new instance of <code>UserExpenses</code>.
     */
    
    private ArrayList<ExpenseRecords> expenses;
    
    /**
     * Constructs a list of ExpenseRecords objects that the user enters every time he/she is
     * logging their records
     */
    public UserExpenses() 
    {
    	expenses = new ArrayList<ExpenseRecords>();
    }
    
    /**
     * addExpenseRecord adds a new ExpenseRecords object
     * @param newRecord the ExpenseRecords object that the user enters
     */
    public void addExpenseRecord(ExpenseRecords newRecord)
    {
    	expenses.add(newRecord);
    }
    
    /**
     * clearList clears the ArrayList of ExpenseRecords
     */
    public void clearList()
    {
    	expenses.clear();
    }
    
    /**
     * size gets the size of an arrayList containing ExpenseRecords
     * @return the size of an arrayList of ExpenseRecords
     */
    public int size()
    {
    	return expenses.size();
    }
    
    /**
     * set will replace an ExpenseRecords object at a certain location with an ExpenseRecords object entered by a user
     * @param index the location of the ExpenseRecords object
     * @param entry the ExpenseRecords object entered by a user to replace the ExpenseRecords object at that index
     */
    public void set(int index, ExpenseRecords entry)
    {
    	expenses.set(index, entry);
    }
    
    /**
     * get will get an ExpenseRecords object at an index given by the user
     * @param index the index number given by the user to get that ExpenseRecords object
     * @return the ExpenseRecords object at an index
     */
    public ExpenseRecords get(int index)
    {
    	return expenses.get(index);
    }
    
    /**
     * indexOf will return the index number of an ExpenseRecords object
     * @param record the ExpenseRecords object that the user chooses
     * @return the index number of record
     */
    public int indexOf(ExpenseRecords record)
    {
    	return expenses.indexOf(record);
    }
    
    /**
     * remove will remove an ExpenseRecords object(if it exists) that the user wants to remove
     * @param temporary the ExpenseRecords object that the user wants to remove
     * @return true if the ExpenseRecords object has been removed or false if it did not exist
     */
    public boolean remove(ExpenseRecords temporary)
    {
    	return expenses.remove(temporary);
    }
    
    /**
     * dailyExpenses shows a record of expenses on a certain day
     * @param date the user wants to check the expenses he paid for on a certain date
     * @return the list of expenses paid on that day
     */
    public String dailyExpenses(String date)
    {
    	ExpenseRecords temporary = new ExpenseRecords(date, "", "", 0.0);
    	String output = "Here is where, what, and how much you spent on " + date + "\n";
    	String company = "";
    	String expense = "";
    	double amount = 0.0;
    	String formattedNumber;
    	double totalAmt = 0.0;
    	for(int i = 0; i < expenses.size(); i++)
    	{
    		if(expenses.get(i).equalsPtTwo(temporary))
    		{
    			company = expenses.get(i).getCompany();
    			expense = expenses.get(i).getExpenseType();
    			amount = expenses.get(i).getExpenseAmt();
    			formattedNumber = String.format("%.2f",amount);
    			totalAmt += Double.parseDouble(formattedNumber);
    			output += (company + ": " + expense + ", $" + formattedNumber + "\n");
    		}	
    	}
    	formattedNumber = String.format("%.2f", totalAmt);
    	output += "The total amount of money you spent is: \n$" + formattedNumber;
    	if(formattedNumber.equals("0.00"))
    		return "You either have never paid for anything on this date or you have entered an invalid date.";
    	else
    		return output;
    }
    
    /**
     * monthlyExpenses shows a list of expenses on a certain month
     * @param month the user wants to check to see the list of expenses
     * @return the list of expenses on a certain month
     */
    public String monthlyExpenses(String month)
    {
    	String output = "Here is where, what, and how much you spent on " + month + "\n";
    	String[] components = month.split("/");
    	String date = "";
    	String monthNum = components[0];
    	String year = components[1];
    	String company = "";
    	String expense = "";
    	double amount = 0.0;
    	String formattedNumber;
    	double totalAmt = 0.0;
    	for(int i = 0; i < expenses.size(); i++)
    	{
    		if(expenses.get(i).getDate().matches(monthNum + "/\\d\\d/" + year))
    		{
    			date = expenses.get(i).getDate();
    			company = expenses.get(i).getCompany();
    			expense = expenses.get(i).getExpenseType();
    			amount = expenses.get(i).getExpenseAmt();
    			formattedNumber = String.format("%.2f",amount);
    			totalAmt += Double.parseDouble(formattedNumber);
    			output += (date + ": " + company + ", " + expense + ", $" + formattedNumber + "\n");
    		}
    	}
    	formattedNumber = String.format("%.2f", totalAmt);
    	output += "The total amount of money you spent is: \n$" + formattedNumber;
    	if(!formattedNumber.equals("0.00"))
    		return output;
    	else
    		return "You either have not paid anything for this month or you entered a month that does not exist.";
    }
    
    /**
     * editOrDeleteRecords will add ExpenseRecords objects with the same date into an arrayList for them to be either
     * edited or deleted. These objects will be created by a string of dailyExpenses that contains all the ExpenseRecords
     * objects on a date.
     * @param day the date the user enters
     * @param input the String of dailyExpenses, which will be split up into parts for certain variables
     */
    public void editOrDeleteRecords(String day, String input)
    {
    	String date = day;
    	String company = "";
    	String expenseType = "";
    	String expenseAmt = "";
    	String records = input.substring(input.indexOf("\n") + 1);
    	double expenseAmount = 0.0;
    	String[] separateRecords = records.split("\n");
    	//The last 2 elements will contain stuff that cannot be split up into companies, expense types, or expense amounts
    	for(int i = 0; i < separateRecords.length - 2; i++)
    	{
    		company = separateRecords[i].substring(0, separateRecords[i].indexOf(":"));
        	expenseType = separateRecords[i].substring(separateRecords[i].indexOf(":") + 2, separateRecords[i].lastIndexOf(","));
        	expenseAmt = separateRecords[i].substring(separateRecords[i].indexOf("$") + 1);
        	expenseAmount = Double.parseDouble(expenseAmt);
        	addExpenseRecord(new ExpenseRecords(date, company, expenseType, expenseAmount));
    	}
    }
    
    /**
     * toString prints out all the content of an arrayList. Each ExpenseRecords object will be printed out with an
     * assigned number
     * @return the numbers and contents of each ExpenseRecords object in the expenses arrayList
     */
    public String toString()
    {
    	int count = 0;
    	String output = "";
    	for(ExpenseRecords e : expenses)
    	{
    		count++;
    		output += count + ": " + e.toString() + "\n";
    	}
    	return output;
    }
    
    /**
     * saveToFile prints out all the ExpenseRecords of an arrayList, with each ExpenseRecords object having 4 lines
     * before being saved to a file
     * @return the content of all ExpenseRecords objects in the expenses arrayList
     */
    public String saveToFile()
    {
    	String output = "";
    	for(ExpenseRecords e : expenses)
    		output += e.saveToFile() + "\n";
    	return output;
    }
    
    /**
     * sort will sort every ExpenseRecords object based on date. If dates are the same, then ExpenseRecords objects will be sorted based on
     * the expense types. If both are the same, sort based on company names
     */
    public void sort()
    {
    	for(int i = 1; i < expenses.size(); i++)
	 		for(int pos = 0; pos < expenses.size() - i; pos++)
	 			if(expenses.get(pos).compareTo(expenses.get(pos+1)) > 0)
	 			{
	 				ExpenseRecords temp = expenses.get(pos);
	 				expenses.set(pos, expenses.get(pos+1));
	 				expenses.set(pos+1, temp);
	 			}
    }

}


