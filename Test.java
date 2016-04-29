import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
/*
    Make sure to compile FaceSpace.java, User.java, and Friendship.java before
        you run Test.java
*/

/**
 *
 * @author David Bickford, Eli Friedman, John Kulp
 * @email drb56@pitt.edu
 */
public class Test {
//    public static String username, password;
    public static int minID;
    
    public static void main(String args[]) throws SQLException{
        String username, password;
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter your Username: ");
        username = reader.next();
        System.out.println("Enter your Password: ");
        password = reader.next();
        


        System.out.println("Registering DB..");
        // Register the oracle driver.  

        System.out.println("Set url..");
        //This is the location of the database.  This is the database in oracle
        //provided to the class
        String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 

        System.out.println("Connect to DB..");
        //create a connection to DB on class3.cs.pitt.edu
        Connection connection;
        try{
            DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());

            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(true);
        }
        catch(Exception e){
            System.out.println("failed to establish a connection to the db");
            return;
        }

        
        try{
                
                
                System.out.println("Testing createUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Users"));
                if(testCreateUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Users"));
                }
                
                minID = findMinID(connection);
//                System.out.println(minID);
                
                System.out.println("Testing createGroup: \n\tNumber of rows before stress test: " + printNumRows(connection, "Groups"));
                if(testCreateGroup(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Groups"));
                }
                
                System.out.println("Testing initiateFriendship: \n\tNumber of rows before stress test: " + printNumRows(connection, "Friends"));
                if(testInitiateFriendship(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Friends"));
                }
                
                System.out.println("Stress testing establishFriendship: ");
                if(testEstablishFriendship(connection)){
                    System.out.println("\tStress test succeeded!");
                }
                
                System.out.println("Testing sendMessageToUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Messages"));
                if(testSendMessageToUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Messages"));
                }
                
                System.out.println("Testing addToGroup: \n\tNumber of rows before stress test: " + printNumRows(connection, "Members"));
                if(testAddToGroup(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Members"));
                }
                
                System.out.println("Testing sendMessageToGroup: \n\tNumber of rows before stress test: " + printNumRows(connection, "Messages"));
                if(testSendMessageToGroup(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Messages"));
                }
                
                System.out.println("Testing searchForUser: \t");
                testSearchForUser(connection);
                
                System.out.println("Testing displayFriends: \t");
                testDisplayFriends(connection);
                
                System.out.println("Testing topMessagers:");
                String top = testTopMessagers(connection);
                if(!top.equals("")){
                    String[] topArr = top.split(" ");
                        System.out.println("\tFinal top message: \n\t" + topArr[0] + topArr[1] + "\n\tMessages: " + topArr[6]);
                }
                
                System.out.println("Testing displayMessages:");
                String displayMessages = testDisplayMessages(connection);
                if(!top.equals("")){
                    String[] dispArr = displayMessages.split(" ");
                        System.out.println("\tFinal top message: \n" 
                                + dispArr[0] + dispArr[1] + dispArr[2] + dispArr[3] + dispArr[4]);
                }
                
                System.out.println("Testing displayNewMessages:");
                String displayNewMessages = testDisplayNewMessages(connection);
                if(!top.equals("")){
                    String[] dispArr = displayNewMessages.split(" ");
                        System.out.println("\tFinal top message: \n" 
                                + dispArr[0] + dispArr[1] + dispArr[2] + dispArr[3] + dispArr[4]);
                }
               
                System.out.println("Testing threeDegrees:");
                if(testThreeDegrees(connection)){
                    System.out.println("\tStress test succeeded!");
                }
                
                
                System.out.println("Testing dropUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Users"));
                if(testDropUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Users"));
                }
                //*/
        }catch(Exception e){
            System.out.println("error connecting");
        }
        finally{
            connection.close();
        }
    }
    
    
    
    public static boolean testDropUser(Connection connection) throws SQLException{
        for(int i = minID; i <= minID+3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.dropUser(connection, i)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testSendMessageToGroup(Connection connection) throws SQLException{
        for(int i = minID; i < minID+500; i++){
                                //System.out.println("createUser");
            if(FaceSpace.sendMessageToGroup(connection, minID+1, i+1, "blerg", "blahblah")){
            }
            else{
//                return false;
            }
        }
        return true;
    }
    
    public static boolean testAddToGroup(Connection connection) throws SQLException{
        for(int i = minID; i <= minID+3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.addToGroup(connection, i, i)){
            }
            else{
//                return false;
            }
        }
        return true;
    }
    
    public static boolean testSendMessageToUser(Connection connection){
        for(int i = minID; i <= minID+2999; i++){
                                //System.out.println("createUser");
            if(FaceSpace.sendMessageToUser(connection, "blahblah", "blerg", minID, minID+1)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testEstablishFriendship(Connection connection) throws SQLException{
        for(int i = 0; i < 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.establishFriendship(connection, minID+i+1)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testInitiateFriendship(Connection connection) throws ParseException, SQLException{
        
        for(int i = 0; i < 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.initiateFriendship(connection, "2015-03-10", 0, minID, minID+i+1)){
            }
//            else{
//                return false;
//            }
        }
        return true;
    }
    
    private static int printNumRows(Connection connection, String tableName){
        
        int numRows = 0;
        try{
                String query = "SELECT COUNT(*) FROM " + tableName;
                
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                
                if(resultSet != null){
                    while (resultSet.next()){
                       numRows = resultSet.getInt(1);
                    }
                    resultSet.close();
                }
                else{
                    System.out.println("Error reading from resultset");
                }
                
            }catch(SQLException Ex) {
                System.out.println("Error running the sample queries.  Machine Error: " +
                   Ex.toString());
            }
        return numRows;
    }
    
    public static boolean testCreateUser(Connection connection) throws SQLException{
        for(int i = 0; i < 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.createUser(connection, "abcde", "abcde", "elkjlkj", "2012-02-24")){
            }
            else{
                return false;
            }
        }
        return true;
    }
            
    public static boolean testCreateGroup(Connection connection){
        for(int i = 0; i < 3000; i++){
                //System.out.println("createGroup");
            if(FaceSpace.createGroup(connection, "blah", "test", i+11)){
            }
            else{
                return false;
            }
        }
        return true;
    }

    public static void testSearchForUser(Connection connection) throws SQLException, IllegalAccessException, ParseException{
        ArrayList<User> results = null;
        for(int i = 0; i < 100; i++){
                //System.out.println("createGroup");
            results = FaceSpace.searchForUser(connection, "jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994");

            //print on the last iteration
            if (i == 99){

                System.out.println("\tThe users found with the search string 'jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994' were:");
                if (results.size() == 0){
                    System.out.println("none");
                }
                for(int z = 0; z < results.size(); z++){
                    System.out.println("\t" + results.get(z).getFname() + " " + results.get(z).getLname() + ", user number " + results.get(z).getUserID());
                }
            }
        }
    }
    
    public static String testTopMessagers(Connection connection) throws SQLException{
        ArrayList<String> list = null;
        for(int i = 0; i <= 100; i++){
                //System.out.println("createUser");
            list = FaceSpace.topMessagers(connection, 10, "2015/01/01" );

        }
        return list.get(list.size()-1);
    }
    
    public static void testDisplayFriends(Connection connection) throws SQLException{
        ArrayList<Friendship> friends = null;
        
        for(int i=0; i<=600; i++){
            friends = FaceSpace.displayFriends(connection, minID+1);
        }
        for(Friendship friend : friends){
            System.out.println("\tthe friendship between " 
                    +  friend.getFriendOne() + " and " + friend.getFriendTwo() + " was established on " + friend.getFriendDate());
        }


    }
    
    public static String testDisplayMessages(Connection connection) throws SQLException{
        ArrayList<String> list = null;
        for(int i = minID; i <= minID+100; i++){
                //System.out.println("createUser");
				//System.out.println(minID);
            list = FaceSpace.displayMessages(connection, minID+1);
				//System.out.println(list);
        }
        return list.get(list.size()-1);
    }
    
    public static String testDisplayNewMessages(Connection connection) throws SQLException{
        ArrayList<String> list = null;
//        for(int i = minID; i <= minID+100; i++){
                //System.out.println("createUser");
            list = FaceSpace.displayNewMessages(connection, minID+1);

//        }
        return list.get(list.size()-1);
    }
    
    public static boolean testThreeDegrees(Connection connection) throws SQLException{
        ArrayList<Integer> list = null;
        int i;
        for(i = 1; i <= 100; i++){
                //System.out.println("createUser");
//				System.out.println("got to for loop");
            list = FaceSpace.threeDegrees(connection, minID+1, minID+i+2 );
//			System.out.println("list "+ i+" ="+ list);
        }
        System.out.println("The middle ids between " + (minID+1) + " and " + (minID+i+2) + " are: ");
        for(int j = 0; j < list.size(); j++){
                System.out.println("\tID: " +list.get(j));
        }
        return list.size() != 0;
    }
    
    private static int findMinID(Connection connection) throws SQLException{
        Statement statement = null;
            ResultSet resultSet = null;
            ArrayList<Integer> id = new ArrayList<Integer>();
//            int id = 0;
            try{
                        String query = "SELECT * FROM Users";

                        statement = connection.createStatement();
                        resultSet = statement.executeQuery(query);
                        

                        if(resultSet != null){
                                while (resultSet.next()){
                                        int message = resultSet.getInt(6);
                                        id.add(message);
                                }
                                resultSet.close();
                                statement.close();
                                
                        }
                        else{
                            
                        }

                }catch(SQLException Ex) {
//                        System.out.println("Error running the sample queries.  Machine Error: blerg" +
//                           Ex.toString());
                        resultSet.close();
                        statement.close();
                }
            int[] blah = new int[id.size()];
            for(int i=0; i<id.size(); i++){
                blah[i] = id.get(i);
            }
            Arrays.sort(blah);
            return blah[0];
        }
    

}
