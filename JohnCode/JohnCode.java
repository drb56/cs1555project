
import java.sql.*;  //import the file containing definitions for the parts
import java.util.ArrayList;
import java.util.Arrays;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import oracle.jdbc.*; //needed by java for database connection and manipulation
import java.lang.reflect.Field;



public class JohnCode {



      public static void main(String args[]) throws SQLException, IllegalAccessException{
            /* Making a connection to a DB causes certain exceptions.  In order to handle
               these, you either put the DB stuff in a try block or have your function
               throw the Exceptions and handle them later. */

            String username, password;
            username = "elf62"; //This is your username in oracle
            password = "3981019"; //This is your password in oracle
            
            System.out.println("Registering DB..");
            // Register the oracle driver.  
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());


            System.out.println("Set url..");
            //This is the location of the database.  This is the database in oracle
            //provided to the class
            String url = "jdbc:oracle:thin:@DESKTOP-2AN1RIL:1521:XE"; 

            System.out.println("Connect to DB..");
            //create a connection to DB on class3.cs.pitt.edu
            Connection connection = DriverManager.getConnection(url, username, password); 
            displayFriends(connection, 2);
            boolean result = sendToGroup(connection, 1, 15, "hey", "I'm sending a test message!");
            searchForUser(connection, "jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994");
            threeDegrees(connection, 3, 12);
            System.out.println("the return of sendToGroup was " + Boolean.toString(result));

            String[] datePatterns = new String[] {
                "dd-MM-yyyy", // ex. 11-09-2009
                "dd/MM/yyyy", // ex. 11/09/2009
            };

            //JohnCode demo = new JohnCode(connection);
            //demo.displayMessages(12);
            try{
                  
            }
            catch(Exception Ex)  {
                  System.out.println("Error running queries.  Machine Error: " +
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