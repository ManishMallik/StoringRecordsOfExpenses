package ExpenseRecords;

import java.io.Serializable;
//This is an object class that will contain elements of accounts. This will be helpful in determining who is who and having users get their own records and manage only theirs.
public class User implements Serializable
{
    
    /**
     * Creates a new instance of <code>User</code>.
     */
    private String realName;
    private String username;
    private String password;
    
    /**
     * Constructs a User object with a name, username, and password
     * @param name the name of the user
     * @param userName the user's username
     * @param pass the user's password
     */
    public User(String name, String userName, String pass) 
    {
    	realName = name;
    	username = userName;
    	password = pass;
    }
    
    /**
     * getUsername returns the username of the user
     * @return the username
     */
    public String getUsername()
    {
    	return username;
    }
    
    /**
     * getRealName returns the name of the user
     * @return the name of the user
     */
    public String getRealName()
    {
    	return realName;
    }
    
    /**
     * getPassword returns the user's password
     * @return the user's password
     */
    public String getPassword()
    {
    	return password;
    }
    
    /**
     * toString returns the user's name, followed by his or her username and password
     * @return the name, username, and password of the user
     */
    public String toString()
    {
    	return realName + "\n" + username + "\n" + password;
    }
    
    /**
     * equals returns true or false if the username the user enters matches with another username that exists
     * @param o the User object that the user enters, which includes the name, username, and password
     * @return true or false if the usernames match or not
     */
    public boolean equals(Object o)
    {
    	User other = (User) o;
    	return this.username.equals(other.username);
    }
    
    /**
     * compareTo returns the difference between the names of different users
     * @param o the object that is passed on in the parameter
     * @return the difference between the name of the user so that the User objects can be ordered
     */
    public int compareTo(Object o)
    {
    	User other = (User) o;
    	return this.realName.compareTo(other.realName);
    }
    
    /**
     * saveToFile saves a user's name, username, and password
     * @return the name, username, and password of a user's account
     */
    public String saveToFile()
    {
    	return realName + "\n" + username + "\n" + password;
    }
}


