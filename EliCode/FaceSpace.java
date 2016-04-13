

import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import oracle.jdbc.*; //needed by java for database connection and manipulation
					
public class FaceSpace {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
	
	//constructor of facespace object 
	public FaceSpace(){
		createUser("abcde", "abcde", "elkjlkj", "2012-02-24");
		createGroup("Eli Test", "Eli Test", 1);
		addToGroup(1,1);
		sendMessageToUser("MessageTEst", "MessageTEst", 1, 1);
		
		//@FaceSpace.sql
	}
	//*************last login time? add every action?
	
	//function to send message to user
	public void sendMessageToUser(String subj, String body, int recipiet, int sender){
		try{
			query = "insert into Messages(subject, msgText, dateSent, senderID, recipientID) values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			
			//formatting time for date sent
			java.util.Date utilDate = new java.util.Date();
			java.sql.Timestamp dateSent = new java.sql.Timestamp(utilDate.getTime());
			
			prepStatement.setString(1, subj); 
			prepStatement.setString(2, body);
			prepStatement.setTimestamp(3, dateSent);
			prepStatement.setInt(4, recipiet);
			prepStatement.setInt(5, sender);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) {
	    System.out.println("Error running the sample queries.  Machine Error: " +
			       Ex.toString());
		}/* catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}*/
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//function to addToGroup
	public void addToGroup(int groupId, int userId){
		try{
			query = "insert into Members values (?,?)";
			prepStatement = connection.prepareStatement(query);
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setInt(1, groupId); 
			prepStatement.setInt(2, userId);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) {
	    System.out.println("Error running the sample queries.  Machine Error: " +
			       Ex.toString());
		}/* catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}*/
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//function to create Group
	public void createGroup(String name, String descr, int membLimit){
		try{
			query = "insert into Groups(name ,description, personLimit ) values (?,?,?)";
			prepStatement = connection.prepareStatement(query);
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setString(1, name); 
			prepStatement.setString(2, descr);
			prepStatement.setInt(3, membLimit);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) {
	    System.out.println("Error running the sample queries.  Machine Error: " +
			       Ex.toString());
		}/* catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}*/
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}
	
	//function to create user. sets last login to current time
	public void createUser(String fname, String lname, String email, String dob){
		try{
			query = "insert into Users(fname, lname, email, dateOfBirth, lastLogin) values (?,?,?,?,?)";
			prepStatement = connection.prepareStatement(query);
			
			//formatting date for birthday
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date dateOfBirth = new java.sql.Date (df.parse(dob).getTime());
			
			//formatting time for last login
			java.util.Date utilDate = new java.util.Date();
			java.sql.Timestamp lastLogin = new java.sql.Timestamp(utilDate.getTime());
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setString(1, fname); 
			prepStatement.setString(2, lname);
			prepStatement.setString(3, email);
			prepStatement.setDate(4, dateOfBirth);
			prepStatement.setTimestamp(5, lastLogin);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
		}
		catch(SQLException Ex) {
	    System.out.println("Error running the sample queries.  Machine Error: " +
			       Ex.toString());
		} catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
			e.toString());
		}
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: "+e.toString());
			}
		}
	}

	public static void main(String args[]) throws SQLException {
		/* Making a connection to a DB causes certain exceptions.  In order to handle
		   these, you either put the DB stuff in a try block or have your function
		   throw the Exceptions and handle them later. */

		String username, password;
		username = "elf62"; //This is your username in oracle
		password = "3981019"; //This is your password in oracle
		
		try{
			System.out.println("Registering DB..");
			// Register the oracle driver.  
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			

			System.out.println("Set url..");
			//This is the location of the database.  This is the database in oracle
			//provided to the class
			String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
			
			System.out.println("Connect to DB..");
			//create a connection to DB on class3.cs.pitt.edu
			connection = DriverManager.getConnection(url, username, password); 
			FaceSpace demo = new FaceSpace();
			
		}
		catch(Exception Ex)  {
			System.out.println("Error connecting to database.  Machine Error: " +
					   Ex.toString());
		}
		finally
		{
			/*
			 * NOTE: the connection should be created once and used through out the whole project;
			 * Is very expensive to open a connection therefore you should not close it after every operation on database
			 */
			connection.close();
		}
    }
	
}