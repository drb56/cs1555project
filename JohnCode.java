
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
        

    // returns an arraylist of friendships that include userID as one of the friendIDs
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

    //sends a message to a group
    public static boolean sendToGroup(Connection conn, int groupID, int senderID, String subject, String message){
        String query = "SELECT userID FROM Members WHERE GroupID = ?";
        String generatedColumns[] = {"userID"};

        ArrayList<Integer> usersForQuery = new ArrayList<Integer>();
        try{
            PreparedStatement statement = conn.prepareStatement(query, generatedColumns);
            statement.setInt(1, groupID);
            ResultSet usersInGroup;

            if (statement.execute()){
                usersInGroup = statement.getResultSet();
            }
            else{
                System.out.println("That group has no members.");
                return false;
            }
            String messageInputString = "(";
            while(usersInGroup.next()){
                usersForQuery.add(usersInGroup.getInt(1));
            }
            for(int i =0; i < usersForQuery.size(); i++){
                messageInputString += usersForQuery.get(i);
                if(i < usersForQuery.size() - 1){
                    messageInputString += ", ";
                }
            }
            messageInputString += ")";
            System.out.println("user ids to send to: " + messageInputString);


        }
        catch(Exception Ex)  {
            System.out.println("Error querying database.  Machine Error: " +
                Ex.toString());
            return false;
        }

        ArrayList<PreparedStatement> inserts = new ArrayList<PreparedStatement>();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Timestamp dateSent = new java.sql.Timestamp(utilDate.getTime());

        for(int i = 0; i < usersForQuery.size(); i++){
            query = "INSERT INTO Messages(subject, msgText, dateSent, senderID, recipientID) VALUES(?, ?, ?, ?, ?)";
            try{
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, subject);
                statement.setString(2, message);
                statement.setTimestamp(3, dateSent);
                statement.setInt(4, senderID);
                statement.setInt(5, usersForQuery.get(i));
                inserts.add(statement);
            }
            catch(Exception Ex)  {
                System.out.println("Error submitting to database.  Machine Error: " +
                    Ex.toString());
                return false;
            }
        }

        boolean succeeded = true;


        for(int i = 0; i < inserts.size(); i++){

            PreparedStatement statement = inserts.get(i);
            try{
                int result = statement.executeUpdate();
                System.out.println("the insert returned a result of:");
                System.out.println(result);
            }
            catch(Exception Ex)  {
                System.out.println("Error submitting to database.  Machine Error: " +
                    Ex.toString());
                return false;
            }
        }
        return succeeded;
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
            boolean result = sendToGroup(connection, 1, 15, "hey", "I'm sending a test message!");
            System.out.println("the return of sendToGroup was " + Boolean.toString(result));

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