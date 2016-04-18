import java.util.Date;

public class User{

	private String fname;
	private String lname;
	private String email;
	private int userID;
	private Date dateOfBirth;
	private Date lastLogin;

	public User(String fname, String lname, String email, int userID, Date dateOfBirth, Date lastLogin){
		fname = fname;
		lname = lname;
		email = email;
		userID = userID;
		dateOfBirth = dateOfBirth;
		lastLogin = lastLogin;
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