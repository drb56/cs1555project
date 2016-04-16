
import java.sql.*;  //import the file containing definitions for the parts
import java.util.ArrayList;
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
        public JohnCode(Connection conn) /*throws ParseException*/{
        }
        

    //assume
    public static ArrayList<Friendship> displayFriends(Connection conn, int userID){
        Statement friendsQuery = null;
        try{
            String query = "SELECT * FROM Friends WHERE userID1 = ? OR userID2 = ?";
            String generatedColumns[] = { "FriendDate",  "FriendStatus", "userID1", "userID2", "friendID"};
            PreparedStatement statement = conn.prepareStatement(query, generatedColumns);

            statement.setInt(1, userID);
            statement.setInt(2, userID);
            
            ResultSet friendships;
            if(statement.execute()){
                friendships = statement.getResultSet();

            }
            else{
                System.out.println("Error with query.  Statement execute returned false.");
                return null;
            }
            ArrayList<Friendship> friendships_list = new ArrayList<Friendship>();

            while(friendships.next()){
                //rs = stmt.getGeneratedKeys();
                String friendDate = friendships.getDate(1).toString();
                boolean status = (friendships.getInt(2) == 0)? false:true;
                Friendship next_friend = new Friendship(friendDate, friendships.getInt(3), friendships.getInt(4), status);

                friendships_list.add(next_friend);

                System.out.println("FriendDate: " + friendships.getDate(1)
                    + "\nFriendStatus: " + friendships.getInt(2)
                    + "\nuserID1: " + friendships.getInt(3)
                    + "\nuserID2: " + friendships.getInt(4)
                    + "\nfriendID: " + friendships.getString(5)
                    + "\n\n");
            }
            return friendships_list;
        }
        catch(Exception Ex)  {
            System.out.println("Error querying database.  Machine Error: " +
                Ex.toString());
            return null;
        }

    }


      public static void main(String args[]) throws SQLException {
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