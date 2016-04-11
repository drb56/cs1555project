

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
	public FaceSpace() throws ParseException{
		createUser("abcde", "abcde", "elkjlkj", "2012-02-24");
		initiateFriendship("2015-03-10", 0, 1, 2);
		
	}
        
        public void initiateFriendship(String friendDate, int friendStatus, int userID1, int userID2) throws ParseException{
            try{
                query = "insert into Friends(friendDate, 0, userID1, userID2) values (?,?,?,?)";
                prepStatement = connection.prepareStatement(query);

                //formatting date for birthday
                java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date date = new java.sql.Date (df.parse(friendDate).getTime());

                //formatting time for last login
                java.util.Date utilDate = new java.util.Date();
                java.sql.Timestamp lastLogin = new java.sql.Timestamp(utilDate.getTime());

                // You need to specify which question mark to replace with a value.
                // They are numbered 1 2 3 etc..
                prepStatement.setDate(1, date);
                prepStatement.setInt(2, friendStatus); 
                prepStatement.setInt(3, userID1);
                prepStatement.setInt(4, userID2);


                // Now that the statement is ready. Let's execute it. Note the use of 
                // executeUpdate for insertions and updates instead of executeQuery for 
                // selections.
                prepStatement.executeUpdate();
            }
            catch(SQLException Ex) {
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