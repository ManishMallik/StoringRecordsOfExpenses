package ExpenseRecords;
//This is an object class for users to access when managing their own records of expenses
public class ExpenseRecords 
{
    /**
     * Creates a new instance of <code>ExpenseRecords</code>.
     */
    
    private String date; 
    private String company; 
    private String expenseType;
	private double expenseAmt;
    
    /**
     * Constructs an ExpenseRecords object with a date, company name, expense type, and the money spent on that expense
     * @param day the date the user spent on
     * @param company the company name
     * @param expenseName the name of the expense
     * @param money the amount of money spent at that expense
     */
    public ExpenseRecords(String day, String company, String expenseName, double money) 
    {
    	date = day;
    	this.company = company;
    	expenseType = expenseName;
    	expenseAmt = money;
    }
    
    /**
     * getDate gives the current date
     * @return the date in mm/dd/yyyy format
     */
    public String getDate()
    {
    	return date;
    }
    
    /**
     * getCompany gives the name of a certain company
     * @return the name of the company
     */
    public String getCompany()
    {
    	return company;
    }
    
    /**
     * getExpenseType returns the type of the expense
     * @return the type of the expense
     */
    public String getExpenseType()
    {
    	return expenseType;
    }
    
    /**
     * getExpenseAmt returns the amount spent on a certain type of expense
     * @return the amount spent on a type of expense
     */
    public double getExpenseAmt()
    {
    	return expenseAmt;
    }
    
    /**
     * toString returns a message of what expense the user spent on and how much was spent on that expense
     * @return the date, company name, expense type, and the amount of money spent
     */
    public String toString()
    {
    	return date + ", " + company + ", " + expenseType + ", $" + String.format("%.2f",expenseAmt);
    }
    //this method will be invoked whenever indexOf is used, so all elements of ExpenseRecords should be the same
    /**
     * equals checks if all of the ExpenseRecords elements are the same
     * @param o the ExpenseRecords object containing the date, company, expense type and money spent for that expense
     * @return true or false if objects are exactly the same
     */
    public boolean equals(Object o)
    {
    	ExpenseRecords other = (ExpenseRecords) o;
    	return this.toString().equals(other.toString());
    }
    //originally this was made as equals method, but because of the indexOf method, I made this a 2nd equals method so that I did not have to change dailyExpenses method
    /**
     * equalsPtTwo checks if two expense types are the same
     * @param o the ExpenseRecords object containing the date
     * @return true or false if the dates are the same
     */
    public boolean equalsPtTwo(Object o)
    {
    	ExpenseRecords other = (ExpenseRecords) o;
    	return this.date.equals(other.date);
    }
    
    /**
     * compareTo compares two expense types to see the difference between them
     * @param o the ExpenseRecords object passed into the parameter to compare its date and expense type to the other 
     * date and expense type. It also compares between two company names
     * @return the difference between the two dates, first based on year, then month and then day. 
     * If they are equal, return the differences between the expense types. If those differences are equal, return the difference between two
     * companies.
     */
    public int compareTo(Object o)
    {
    	ExpenseRecords other = (ExpenseRecords) o;
    	if(this.date.equals(other.date))
    		if(this.expenseType.equals(other.expenseType))
    			return this.company.compareTo(other.company);
    		else
    			return this.expenseType.compareTo(other.expenseType);
    	else
    	{
    		String[] thisComponents = this.date.split("/");
    		String[] otherComponents = other.date.split("/");
    		String thisYr = thisComponents[2];
    		String otherYr = otherComponents[2];
    		if(thisYr.equals(otherYr))
    			return this.date.compareTo(other.date);
    		else
    			return thisYr.compareTo(otherYr);
    	}
    }
    
    /**
     * saveToFile saves the expense type and the amount spent for that expense as part of recording the expenses
     * @return the date, company name, expense type, and the amount of money spent
     */
    public String saveToFile()
    {
    	return date + "\n" + company + "\n" + expenseType + "\n" + expenseAmt;
    }
    
}
