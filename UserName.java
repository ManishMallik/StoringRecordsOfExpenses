package ExpenseRecords;

import java.io.*;
import java.util.*;
//This is a list class containing User objects. These methods will be used whenever users are creating accounts or logging in.
public class UserName implements Serializable
{
        
    /**
     * Creates a new instance of <code>UserName</code>.
     */
     
    private ArrayList<User> userName;
    
    /**
     * Constructs an arrayList of User objects that are created whenever the users create their own accounts
     */
    public UserName() 
    {
    	userName = new ArrayList<User>();
    }
    
    /**
     * size returns the size of the arrayList userName
     * @return the size of userName
     */
    public int size()
    {
    	return userName.size();
    }
    
    /**
     * userNameExists will check if a user's input for the username exists or not
     * @param temporary the User object created by a user, which consists of a name, username, and password
     * @return true if the username does exist
     */
    public boolean userNameExists(User temporary)
    {
    	for(User u : userName)
    		if(u.equals(temporary))
    		{
    			return true;
    		}
    	return false;
    }
    
    /**
     * userNameLocation will check the location of a username that the user inputs
     * @param temporary the User object created by a user, which consists of a name, username, and password
     * @return the index number of the username or -1 if the username does not exist in the list of User objects
     */
    public int userNameLocation(User temporary)
    {
    	int index = -1;
    	for(int i = 0; i < userName.size(); i++)
    		if(userName.get(i).equals(temporary))
    		{
    			index = i;
    			return index;
    		}
    	return index;
    }
    
    /**
     * addAccount will add a new account(a User object) to the arrayList of User objects
     * @param temporary the User object that the user creates after creating a new account
     */
    public void addAccount(User temporary)
    {
    	userName.add(temporary);
    }
    
    /**
     * passwordMatch will check to see if a password that the user enters matches with the password based on the username
     * entered by the user
     * @param temporary the User object that is created after the user enters the username and password(the name is blank)
     * @return true if the username exists and if the password that goes with the username matches with the user's
     * password input
     */
    public boolean passwordMatch(User temporary)
    {
    	if(userNameLocation(temporary) == -1)
    	{
    		return false;
    	}
    	else
    	{
    		return (userName.get(userNameLocation(temporary)).getPassword().equals(temporary.getPassword()));
    	}
    }
    
    /**
     * getName allows the arrayList userName to access the name of a user from a User object based on the index
     * @param i the index number that the userName array list will use to get the User object and then get the real name
     * @return the real name of the user chosen by the index
     */
    public String getName(int i)
    {
    	return userName.get(i).getRealName();
    }
    
    /**
     * getUsername allows the arrayList userName to access the username of a user from a User object based on the index
     * @param i the index number that the userName array list will use to get the User object and then get the username
     * @return the username of the user chosen by the index
     */
    public String getUserName(int i)
    {
    	return userName.get(i).getUsername();
    }
    
    /**
     * getPassword allows the arrayList userName to access the password of a user from a User object based on the index
     * @param i the index number that the userName array list will use to get the User object and then get the password
     * @return the password of the user chosen by the index
     */
    public String getPassword(int i)
    {
    	return userName.get(i).getPassword();
    }
    
    /**
     * toString goes through each User object of the arrayList userName, formatting each User object into a string output
     * and then adding a new line after each User object is printed to the console
     * @return the name, username, and password of each User object
     */
    public String toString()
    {
    	String output = "";
    	for(User u : userName)
    	{
    		output += u.toString() + "\n";
    	}
    	return output;
    }
    
    /**
     * saveToFile goes through each User object of the arrayList userName, formatting each User object into a string 
     * output, adding a new line after each User object is printed to a file of accounts
     * @return the name, username, and password of each User object
     */
    public String saveToFile()
    {
    	String output = "";
    	for(User u : userName)
    	{
    		output += u.saveToFile() + "\n";
    	}
    	return output;
    }
}

