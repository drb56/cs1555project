import java.sql.*;  //import the file containing definitions for the parts
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import oracle.jdbc.*; //needed by java for database connection and manipulation
public class User{

	private String fname;
	private String lname;
	private String email;
	private int userID;
	private Date dateOfBirth;
	private Date lastLogin;

	public User(String fname, String lname, String email, int userID, Date dateOfBirth, Date lastLogin){
			this.fname = fname;
			this.lname = lname;
			this.email = email;
			this.userID = userID;
			this.dateOfBirth = dateOfBirth;
			this.lastLogin = lastLogin;
	}
	public String getFname(){
			return fname;
	}
	public String getLname(){
			return lname;
	}
	public String getEmail(){
			return email;
	}
	public int getUserID(){
			return userID;
	}
	public Date getDateOfBirth(){
			return dateOfBirth;
	}
	public Date getLastLogin(){
			return lastLogin;
	}

}
