

import java.sql.*;  //import the file containing definitions for the parts
import java.text.ParseException;
import oracle.jdbc.*; //needed by java for database connection and manipulation
                              
public class JohnCode {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using
      
      //constructor of facespace object 
      public FaceSpace() throws ParseException{
            
      }
        

	//assume
	public static void displayFriends(connection conn, int userID){
		Statement friendsQuery = null;
		try{
            query = "SELECT FROM Friends WHERE userID1 = ? OR userID2 = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setInt(userID);
            statement.setInt(userID);

            ResultSet frienships = stmt.executeQuery(statement);

            while(frienships.next()){
            	
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

                  displayFriends(connection, 2)
                  
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