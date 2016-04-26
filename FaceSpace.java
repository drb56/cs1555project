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

public class FaceSpace {
//    private static Connection connection; //used to hold the jdbc connection to the DB
//    private Statement statement; //used to create an instance of the connection
//    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
//    private ResultSet resultSet; //used to hold the result of your query (if one
	// exists)
//    private String query;  //this will hold the query we are using
	
	//constructor of facespace object 
	public FaceSpace() throws ParseException, SQLException{
			
	}
	
	public static ArrayList<String> topMessagers(Connection connection, int users, String udate){
	try{
		
		//to store userid and number of messages recieved/sent
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		
		//formatting time for date
		DateFormat formatter;
					formatter = new SimpleDateFormat("dd/MM/yyyy");
					Date fdate = (Date) formatter.parse(udate);
//                        java.sql.Date newDate = convertFromJAVADateToSQLDate(fdate);
		java.sql.Timestamp tsdate = new java.sql.Timestamp(fdate.getTime());
		
		Statement statement = connection.createStatement(); //create an instance
		//String selectQuery = "select userid, ttlmsggs from( select userid, (cntsid + cntrid) as ttlmsggs from( select * from (select senderID as userid, count(senderID) as cntsid from Messages where dateSent >= ? group by senderID ) natural join (select recipientID as userid, count(recipientID) as cntrid from Messages where dateSent >= ? group by recipientID ) ) order by ttlmsggs desc ) where rownum <= ?" ;
		String selectQuery = "select senderID as userid, count(senderID) as cntsid from Messages where dateSent >= ? group by senderID";
		PreparedStatement prepStatement = connection.prepareStatement(selectQuery);
		
		prepStatement.setTimestamp(1, tsdate);
		ResultSet resultSet = prepStatement.executeQuery();
		
		
		while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
		{
			hm.put(resultSet.getInt(1), resultSet.getInt(2));
		}
		resultSet.close();
		
		
		//get count from recipients
		statement = connection.createStatement(); //create an instance
		//String selectQuery = "select userid, ttlmsggs from( select userid, (cntsid + cntrid) as ttlmsggs from( select * from (select senderID as userid, count(senderID) as cntsid from Messages where dateSent >= ? group by senderID ) natural join (select recipientID as userid, count(recipientID) as cntrid from Messages where dateSent >= ? group by recipientID ) ) order by ttlmsggs desc ) where rownum <= ?" ;
		selectQuery = "select recipientID as userid, count(recipientID) as cntsid from Messages where dateSent >= ? group by recipientID";
		prepStatement = connection.prepareStatement(selectQuery);
		
		prepStatement.setTimestamp(1, tsdate);
		resultSet = prepStatement.executeQuery();
		
		while (resultSet.next()) //this not only keeps track of if another record
		//exists but moves us forward to the first record
		{
			int userid = resultSet.getInt(1);
			int newcount = resultSet.getInt(2);
			
			if(hm.get(userid) == null){
				hm.put(resultSet.getInt(1), resultSet.getInt(2));
			}
			else{
				int oldcount = hm.get(userid);
				newcount += oldcount;
				hm.put(userid, newcount);
			}
			
			
		}
		resultSet.close();
		int count = 0;
		
		System.out.println("Top messegers are:");

		ArrayList<String> resultStrings = new ArrayList<String>();
		
		while(count != users){
			int maxValueInMap=(Collections.max(hm.values()));// This will return max value in the Hashmap
			for (Map.Entry<Integer, Integer> entry : hm.entrySet()) {  // Itrate through hashmap
				if (entry.getValue()==maxValueInMap) {
					count++;
					resultStrings.add("UserID: " +entry.getKey() + " sent and recieved messages: " + entry.getValue());
					System.out.println("UserID: " +entry.getKey() + " sent and recieved messages: " + entry.getValue());     // Print the key with max value
					if(count == users){
						break;
					}
				}
			}
			hm.values().removeAll(Collections.singleton(maxValueInMap));
		}
					prepStatement.close();
					statement.close();
		return resultStrings;
	}
	catch(SQLException Ex) {
		System.out.println("Error running the sample queries.  Machine Error: " +
				   Ex.toString());
		return null;
	} catch (ParseException e) {
		System.out.println("Error parsing the date. Machine Error: " +
		e.toString());
		return null;
	}
}
	
	//function to send message to user
	public static boolean sendMessageToUser(Connection connection, String subj, String body, int recipiet, int sender){
		try{
			String query = "insert into Messages(subject, msgText, dateSent, senderID, recipientID) values (?,?,?,?,?)";
			PreparedStatement prepStatement = connection.prepareStatement(query);
			
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
			prepStatement.close();
			return true;
		}
		catch(SQLException Ex) {
			System.out.println("Error running the sample queries.  Machine Error: " +
				   Ex.toString());
			return false;
		}
	}
	
	//function to addToGroup
	public static boolean addToGroup(Connection connection, int groupId, int userId){
		try{
			String query = "insert into Members values (?,?)";
			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setInt(1, groupId); 
			prepStatement.setInt(2, userId);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
			prepStatement.close();
			return true;
		}
		catch(SQLException Ex) {
		System.out.println("Error running the sample queries.  Machine Error: " +
				   Ex.toString());
			return false;
		}
	}
	
	//function to create Group
	public static boolean createGroup(Connection connection, String name, String descr, int membLimit){
		try{
			String query = "insert into Groups(name, description, personLimit ) values (?,?,?)";
			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			// You need to specify which question mark to replace with a value.
			// They are numbered 1 2 3 etc..
			prepStatement.setString(1, name); 
			prepStatement.setString(2, descr);
			prepStatement.setInt(3, membLimit);
			
			
			// Now that the statement is ready. Let's execute it. Note the use of 
			// executeUpdate for insertions and updates instead of executeQuery for 
			// selections.
			prepStatement.executeUpdate();
						prepStatement.close();
						return true;
		}
		catch(SQLException Ex) {
					System.out.println("Error running the sample queries.  Machine Error: " +
				   Ex.toString());
					return false;
		}
	}
		
		

	//This function takes two user IDs userID1 and userID2 and finds a list of friends that connect them through friendship relationships
	//returns an ArrayLists of integers representing the IDs of the connecting friends
	public static ArrayList<Integer> threeDegrees(Connection conn, int userID1, int userID2) throws SQLException{

		//first, get all the firiends of a user
		Statement friendsQuery = null;
		String query = "SELECT * FROM Friends WHERE userID1 = ? OR userID2 = ?";
		String generatedColumns[] = { "FriendDate",  "FriendStatus", "userID1", "userID2", "friendID"};
		PreparedStatement statement = conn.prepareStatement(query, generatedColumns);

		statement.setInt(1, userID1);
		statement.setInt(2, userID1);
		
		ResultSet friendships;
		if(statement.execute()){
			friendships = statement.getResultSet();

		}
		else{
			System.out.println("No friends found for initial user.");
			return null;
		}
		ArrayList<Integer> userIDs = new ArrayList<Integer>();

		while(friendships.next()){
			//rs = stmt.getGeneratedKeys();
			int friendID1 = friendships.getInt(3);
			int friendID2 = friendships.getInt(4);

			if(friendID1 == userID1){
				userIDs.add(friendID2);
			}
			else{
				userIDs.add(friendID1);
			}
		}

		//create query to see if the target friend is a friend of any of these users


		String friends_openings = "";
		for(int i = 0; i < userIDs.size(); i++){
			friends_openings += "?";
			if(i != userIDs.size() -1){
				friends_openings += ", ";
			}
		}
		System.out.println(userIDs);
		System.out.println(userID2);

		query = "SELECT userID1, userID2 FROM Friends WHERE userID1 IN ( " + friends_openings + " ) AND userID2 = ?  OR userID2 IN ( " + friends_openings + " ) AND userID1 = ? ";
		
		String generatedColumns2[] = {"userID1", "userID2"};

		PreparedStatement secondQuery = conn.prepareStatement(query, generatedColumns2);

		String queryToPrint = query;
		System.out.println(query);

		int i = 1;
		int z = 0;



		for(i=1; i <= userIDs.size(); i++){
			System.out.println("setting ? " + i + " to " + userIDs.get(i-1));
			secondQuery.setInt(i, userIDs.get(i-1));
			queryToPrint = queryToPrint.replaceFirst("\\?", userIDs.get(i-1).toString());
		}
		System.out.println("setting ? " + i + " to " + userID2);
		secondQuery.setInt(i, userID2);
		queryToPrint = queryToPrint.replaceFirst("\\?", userID2 +"");
		i++;
		for(z=0; z < userIDs.size(); i++, z++){
			System.out.println("setting ? " + i + " to " + userIDs.get(z));
			secondQuery.setInt(i, userIDs.get(z));
			queryToPrint = queryToPrint.replaceFirst("\\?", userIDs.get(z).toString());
		}
		System.out.println("setting ? " + i + " to " + userID2);
		secondQuery.setInt(i, userID2);
		queryToPrint = queryToPrint.replaceFirst("\\?", userID2 +"");

		System.out.println("running query: " + queryToPrint);
		System.out.println(queryToPrint);


		ResultSet secondResult;

		ArrayList<Integer> middle_friends = new ArrayList<Integer>();

		if(secondQuery.execute()){
			secondResult = secondQuery.getResultSet();

			while(secondResult.next()){
				int friendID1 = secondResult.getInt(1);
				int friendID2 = secondResult.getInt(2);
				if(friendID1 != userID2){
					middle_friends.add(friendID1);
				}
				else{
					middle_friends.add(friendID2);
				}
			}
		}else{
			System.out.println("the user was not in the initial user's friends' friends list");
			return null;
		}


		String middle_friends_text = "";
		for(int v = 0; v < middle_friends.size(); v++){
			middle_friends_text += middle_friends.get(v);
			if(v != middle_friends.size() -1){
				middle_friends_text += ", ";
			}
		}

		System.out.println("The intermediary friends to get from " + userID1 + " to " + userID2 + " are [" + middle_friends_text + "]");

		return middle_friends;


	}

	//takes a search string
	//returns an ArrayList of User objects that were found by searching terms in the search string
	public static ArrayList<User> searchForUser(Connection conn, String searchString) throws SQLException, IllegalAccessException, ParseException{

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

			Date utilDate = new java.util.Date();

			Date parsedDate;
			Integer parsedInt;
			java.sql.Timestamp tsdate = null;
			DateFormat formatter;
			
			parsedDate = parseDate(elements[i], datePatterns);
			
			
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date fdate = (Date)parseDate(elements[i], datePatterns);
//            Date fdate = (Date) formatter.parse(elements[i]);
			
			
			
			try{
				if(fdate != null){
					tsdate = new java.sql.Timestamp(fdate.getTime());
				}
				if(parsedDate != null){
//                    parsed_dates.add(convertFromJAVADateToSQLDate(parsedDate));
					parsed_dates.add(tsdate);
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
			java.sql.Date date = new java.sql.Date(parsed_dates.get(z).getTime());
			statement.setDate(i, date);
			queryToPrint = queryToPrint.replaceFirst("\\?", parsed_dates.get(z).toString());
		}
		for(z=0; z < parsed_numbers.size(); i++, z++){
			statement.setInt(i, parsed_numbers.get(z));
			queryToPrint = queryToPrint.replaceFirst("\\?", parsed_numbers.get(z).toString());
		}


		System.out.println(queryToPrint);

		ArrayList<User> foundUsers = new ArrayList<User>();

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


				foundUsers.add(new User(fname, lname, email, id, dob, lastLogin));

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
			return new ArrayList<User>();
		}

		return foundUsers;

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
        public static boolean sendMessageToGroup(Connection conn, int groupID, int senderID, String subject, String message){
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
                                date = dateFormat.parse(dateString);
                                success = true;
                                break;
                        }
                        catch(ParseException e)
                        {

                        }
                }

                return date;
        }

        public static boolean dropUser(Connection connection, int userID){
                try{
                        String query = "DELETE FROM users WHERE userID = " + Integer.toString(userID);
                        Statement prepStatement = connection.prepareStatement(query);
                        prepStatement.executeUpdate(query);
                        prepStatement.close();
                        return true;
                }catch(SQLException Ex) {
                        System.out.println("Error running the sample queries.  Machine Error: " +
                           Ex.toString());
                        return false;
                }


        }

        public static boolean establishFriendship(Connection connection, int userID) throws SQLException{
                try{
                        String query = "UPDATE friends SET friendStatus = 1 WHERE friendID = " + Integer.toString(userID);
                        Statement prepStatement = connection.prepareStatement(query);
                        prepStatement.executeUpdate(query);
                        prepStatement.close();
                        return true;
                }catch(SQLException Ex) {
                        System.out.println("Error running the sample queries.  Machine Error: " +
                           Ex.toString());
                        return false;
                }
        }

        public static ArrayList<String> displayNewMessages(Connection connection, int userID){

                try{
                        String ID = Integer.toString(userID);
                        String query = "SELECT M.subject, M.msgText, M.dateSent, M.senderID, M.recipientID, M.msgID\n" +
                                                "FROM Messages M, Users U\n" +
                                                "WHERE M.dateSent > U.lastLogin AND M.recipientID = " + ID;

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);
                        ArrayList<String> newMessages = new ArrayList<String>();

                        if(resultSet != null){
                                while (resultSet.next()){
                                        String newMessage = "\n\nmsgID: " + resultSet.getString(6)
                                                        + "\nSenderID: " + resultSet.getInt(4)
                                                        + "\nRecipientID: " + resultSet.getInt(5)
                                                        + "\nDateSent: " + resultSet.getDate(3)
                                                        + "\nSubject: " + resultSet.getString(1)
                                                        + "\nMessageText: " + resultSet.getString(2);
                                        newMessages.add(newMessage);
                                        System.out.println(newMessage);
                                }
                                resultSet.close();
                                statement.close();
                                return newMessages;
                        }
                        else{
                                System.out.println("Sorry, there is no user with that ID.");
                                return new ArrayList<String>();
                        }

                }catch(SQLException Ex) {
                        System.out.println("Error running the sample queries.  Machine Error: blerg" +
                           Ex.toString());
                        return null;
                }
        }

        public static ArrayList<String> displayMessages(Connection connection, int userID){
                try{
                        String query = "SELECT * FROM messages WHERE recipientID = " + Integer.toString(userID);

                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(query);


                        ArrayList<String> messages = new ArrayList<String>();

                        if(resultSet != null){
                                while (resultSet.next()){
                                        String message = "\n\nmsgID: " + resultSet.getString(6)
                                                        + "\nSenderID: " + resultSet.getInt(4)
                                                        + "\nRecipientID: " + resultSet.getInt(5)
                                                        + "\nDateSent: " + resultSet.getDate(3)
                                                        + "\nSubject: " + resultSet.getString(1)
                                                        + "\nMessageText: " + resultSet.getString(2);
                                        messages.add(message);

                                }
                                resultSet.close();
                                statement.close();
                                return messages;
                        }
                        else{
                                System.out.println("Sorry, there is no user with that ID.");
                                return new ArrayList<String>();
                        }

                }catch(SQLException Ex) {
                        System.out.println("Error running the sample queries.  Machine Error: blerg" +
                           Ex.toString());
                        return null;
                }
        }


        public static boolean initiateFriendship(Connection connection, String friendDate, int friendStatus, int userID1, int userID2) throws ParseException{
			try{
				String query = "insert into Friends(friendDate, friendStatus, userID1, userID2) values (?,?,?,?)";
				

				//formatting date for birthday
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
				java.sql.Date date = new java.sql.Date (df.parse(friendDate).getTime());
				PreparedStatement prepStatement = connection.prepareStatement(query);
				prepStatement.setDate(1, date);
				prepStatement.setInt(2, friendStatus); 
				prepStatement.setInt(3, userID1);
				prepStatement.setInt(4, userID2);


				prepStatement.executeUpdate();
				prepStatement.close();
				return true;
			}
			catch(SQLException Ex) {
				return false;
			}
		}
	
	//function to create user. sets last login to current time
	public static boolean createUser(Connection connection, String fname, String lname, String email, String dob){
		try{
			String query = "insert into Users(fname, lname, email, dateOfBirth, lastLogin) values (?,?,?,?,?)";
						
			//formatting time for last login
			java.util.Date utilDate = new java.util.Date();
			java.sql.Timestamp lastLogin = new java.sql.Timestamp(utilDate.getTime());
			//formatting date for birthday
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");
			java.sql.Date dateOfBirth = new java.sql.Date (df.parse(dob).getTime());
			PreparedStatement prepStatement = connection.prepareStatement(query);
			
			
			prepStatement.setString(1, fname); 
			prepStatement.setString(2, lname);
			prepStatement.setString(3, email);
			prepStatement.setDate(4, dateOfBirth);
			prepStatement.setTimestamp(5, lastLogin);
			
			prepStatement.executeUpdate();
			prepStatement.close();
			return true;
		}
		catch(SQLException Ex) {
			System.out.println("Error running the sample queries.  Machine Error: " +
				Ex.toString());
			return false;
		} catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " +
				e.toString());
			return false;
		}
	}

	public static void main(String args[]) throws SQLException {

		String username, password;
		username = "drb56"; //This is your username in oracle
		password = "Robert098$"; //This is your password in oracle
		
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
			Connection connection = DriverManager.getConnection(url, username, password); 
//			FaceSpace demo = new FaceSpace();
//                        System.out.println("dropUser");
//                        dropUser(connection, 17);
//                        System.out.println("createUser");
//                        createUser(connection, "abcde", "abcde", "elkjlkj", "2012-02-24");
//                        System.out.println("initiateFriendship");
//                        initiateFriendship(connection, "2015-03-10", 0, 8, 9);
//                        System.out.println("establishFriendship");
//                        establishFriendship(connection, 201);
//                        System.out.println("displayMessages");
//                        displayMessages(connection, 64);
//                        System.out.println("distplayNewMessages");
//                        displayNewMessages(connection, 34);
//                        System.out.println("sendMessageToGroup");
//                        sendMessageToGroup(connection, 3, 12, "blerg", "blahblah");
//                        System.out.println("displayFriends");
//                        displayFriends(connection, 8);
//                        System.out.println("searchForUser");
//                        searchForUser(connection, "jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994");
//                        System.out.println("threeDegrees");
//                        threeDegrees(connection, 3, 12);
//                        System.out.println("createGroup");
//                        createGroup(connection, "blah", "test", 30);
//                        System.out.println("addToGroup");
//                        addToGroup(connection, 6, 8);
//                        System.out.println("sendMessageToUser");
//                        sendMessageToUser(connection, "blahblah", "blerg", 9, 8);
//                        System.out.println("topMessagers");
//                        topMessagers(connection, 3, "2015/01/01");
						connection.close();
			
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
//			connection.close();
		}
	}
		
		
	
	public static  class User{

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
	
	public static class Friendship{

		private String friendDate;
		private int friend1;
		private int friend2;
		private boolean friendStatus;

		public Friendship(String date, int friend1, int friend2, boolean status){
				friendDate = date;
				friend1 = friend1;
				friend2 = friend2;
				status = status;
		}
		public String getFriendDate(){
				return friendDate;
		}
		public int getFriendOne(){
				return friend1;
		}
		public int getFriendTwo(){
				return friend2;
		}
		public boolean isAccepted(){
				return friendStatus;
		}

	}

}
