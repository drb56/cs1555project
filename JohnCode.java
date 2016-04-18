
import java.sql.*;  //import the file containing definitions for the parts
import java.util.ArrayList;
import java.util.Arrays;
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


    public static java.sql.Date convertFromJAVADateToSQLDate(java.util.Date javaDate) {
        java.sql.Date sqlDate = null;
        if (javaDate != null) {
            sqlDate = new Date(javaDate.getTime());
        }
        return sqlDate;
    }

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
        ArrayList<String> parsed_strings = new ArrayList<String>(Arrays.asList(elements));

        String[] datePatterns = new String[] {
            "dd-MM-yyyy", // ex. 11-09-2009
            "dd/MM/yyyy", // ex. 11/09/2009
        };

        for(int i = 0; i < elements.length; i++){

            java.util.Date utilDate = new java.util.Date();

            java.util.Date parsedDate;
            Integer parsedInt;

            parsedDate = parseDate(elements[i], datePatterns);
            try{
                if(parsedDate != null){
                    parsed_dates.add(convertFromJAVADateToSQLDate(parsedDate));
                    System.out.println("parsed a date with: ");
                    System.out.println(elements[i]);
                }
            }catch(Exception e){
                System.out.println("failed to parse a date with: ");
                System.out.println(elements[i]);
            }


            try{
                parsedInt = Integer.parseInt(elements[i]);
                parsed_numbers.add(parsedInt);
            }catch(NumberFormatException e){
                //Number not found in the string
            }
        }


        String varchar_openings = "";
        for(int i = 0; i < parsed_strings.size(); i++){
            varchar_openings += "?";
            if(i != parsed_strings.size() -1){
                varchar_openings += ", ";
            }
        }
        String date_openings = "";
        for(int i = 0; i < parsed_dates.size(); i++){
            date_openings += "?";
            if(i != parsed_dates.size() -1){
                date_openings += ", ";
            }
        }
        String numbers_openings = "";
        for(int i = 0; i < parsed_numbers.size(); i++){
            numbers_openings += "?";
            if(i != parsed_numbers.size() -1){
                numbers_openings += ", ";
            }
        }

        if(parsed_dates.size() ==0)parsed_dates.add(null);
        if(parsed_strings.size() ==0)parsed_strings.add(null);
        if(parsed_numbers.size() ==0)parsed_numbers.add(null);



        String query = "SELECT * FROM Users WHERE fname IN (" + varchar_openings + ") OR Lname IN (" + varchar_openings +
                        ") OR email IN (" + varchar_openings + ") OR dateOfBirth IN (" + date_openings + ") OR userID IN (" + numbers_openings + ")";


        String queryToPrint = query;

        String generatedColumns[] = { "fname",  "lname", "email", "dateOfBirth", "lastLogin", "userID"};
        PreparedStatement statement = conn.prepareStatement(query, generatedColumns);

        int i = 1;
        int z = 0;


        System.out.println("length of parsed strings is: " + parsed_strings.size() + ", length of varchar openings is: " + varchar_openings.length());

        for(i=1; i <= parsed_strings.size(); i++){
            statement.setString(i, parsed_strings.get(i-1));
            queryToPrint = queryToPrint.replaceFirst("\\?", parsed_strings.get(i-1));
        }
        for(z=0; z < parsed_strings.size(); i++, z++){
            statement.setString(i, parsed_strings.get(z));
            queryToPrint = queryToPrint.replaceFirst("\\?", parsed_strings.get(z));
        }
        for(z=0; z < parsed_strings.size(); i++, z++){
            statement.setString(i, parsed_strings.get(z));
            queryToPrint = queryToPrint.replaceFirst("\\?", parsed_strings.get(z));
        }
        for(z=0; z < parsed_dates.size(); i++, z++){
            statement.setDate(i, parsed_dates.get(z));
            queryToPrint = queryToPrint.replaceFirst("\\?", parsed_dates.get(z).toString());
        }
        for(z=0; z < parsed_numbers.size(); i++, z++){
            statement.setInt(i, parsed_numbers.get(z));
            queryToPrint = queryToPrint.replaceFirst("\\?", parsed_numbers.get(z).toString());
        }


        System.out.println(queryToPrint);

        ResultSet users;

        if(statement.execute()){
            users = statement.getResultSet();
            while(users.next()){

                String fname = users.getString(1);
                String lname = users.getString(2);
                String email = users.getString(3);
                Date dob = users.getDate(4);
                Timestamp lastLogin = users.getTimestamp(5);
                int id = users.getInt(6);



                System.out.println("fname: " + fname
                    + "\nlname: " + lname
                    + "\nemail: " + email
                    + "\ndate of birth: " + dob
                    + "\nid: " + id
                    + "\n\n");

            }
        }
        else{
            //no users found
            System.out.println("no users found!");
            return null;
        }

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
            searchForUser(connection, "jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994");
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