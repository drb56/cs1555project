
import java.sql.*;  //import the file containing definitions for the parts
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import oracle.jdbc.*; //needed by java for database connection and manipulation
import java.lang.reflect.Field;



                  
public class JohnCode {

    /**
     * @param dateString An input String, presumably from a user or a database table.
     * @param formats An array of date formats that we have allowed for.
     * @return A Date (java.util.Date) reference. The reference will be null if 
     *         we could not match any of the known formats.
     */
    private static java.util.Date parseDate(String dateString, String[] formats)
    {
        java.util.Date date = null;
        boolean success = false;

        for (int i = 0; i < formats.length; i++)
        {
            String format = formats[i];
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);

            try
            {
                // parse() will throw an exception if the given dateString doesn't match
                // the current format
                date = dateFormat.parse(dateString);
                success = true;
                break;
            }
            catch(ParseException e)
            {
                // don't do anything. just let the loop continue.
                // we may miss on 99 format attempts, but match on one format,
                // but that's all we need.
            }
        }

        return date;
    }


    //Method: Set properties of the ooject accessible.
    public static  Field getFieldByName(Field[] campos, String name) {
        Field f = null;
        for (Field campo : campos) {
            campo.setAccessible(true);
            if (campo.getName().equals(name)) {
                f = campo;
                break;
            }
        }
        return f;
    }


    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one
    // exists)
    private String query;  //this will hold the query we are using

    //constructor of facespace object 
    public JohnCode(Connection conn) /*throws ParseException*/{
    }
        

    public static User searchForUser(Connection conn, String searchString) throws SQLException, IllegalAccessException{

        Array varchars;
        Array dates;
        Array numbers;

        String[] elements = searchString.split(" ");

        ArrayList<Date> parsed_dates = new ArrayList<Date>();
        ArrayList<Integer> parsed_numbers = new ArrayList<Integer>();

        String[] datePatterns = new String[] {
            "ddMMMyy",    // ex. 11Mar09
            "dd-MM-yyyy", // ex. 11-09-2009
            "dd/MM/yyyy", // ex. 11/09/2009
        };

        for(int i = 0; i < elements.length; i++){

            ArrayList<PreparedStatement> inserts = new ArrayList<PreparedStatement>();
            java.util.Date utilDate = new java.util.Date();

            java.util.Date parsedDate;
            Integer parsedInt;

            parsedDate = parseDate(elements[i], datePatterns);

            try{
                parsedInt = Integer.parseInt(elements[i]);
            }catch(NumberFormatException e){
                //Number not found in the string
                parsedInt = null;
            }
        }
        Integer[] parsed_numbers_array = parsed_numbers.toArray(new Integer[parsed_numbers.size()]);
        java.util.Date[] parsed_dates_array = parsed_dates.toArray(new java.util.Date[parsed_dates.size()]);


        OracleConnection oracleConnection = (OracleConnection) conn;

        System.out.println(oracleConnection.toString());

        if(elements.length > 0){
            varchars = oracleConnection.createARRAY("VARCHAR", elements);
        }else{
            varchars = null;
        }

        if(parsed_dates_array.length > 0){
            //dates = oracleConnection.createARRAY("DATE", parsed_dates_array);
        }else{
            dates = null;
        }

        if(parsed_numbers_array.length > 0){
            //numbers = oracleConnection.createARRAY("NUMBER", parsed_numbers_array);
        }else{
            numbers = null;
        }

        String query = "SELECT * FROM Friends WHERE userID1 = ? OR userID2 = ?";
        String generatedColumns[] = { "FriendDate",  "FriendStatus", "userID1", "userID2", "friendID"};
        PreparedStatement statement = conn.prepareStatement(query, generatedColumns);

        System.out.println("this is the generated query:");
        System.out.println(statement);

        return null;



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
            searchForUser(connection, "jim jones hello@yahoo.com dude 25 10-19-1994");
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