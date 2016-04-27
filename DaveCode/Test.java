
//import static FaceSpace;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 *
 * @author David Bickford
 * @email drb56@pitt.edu
 */
public class Test {
//    public static String username, password;
    
    
    public static void main(String args[]){
        String username, password;
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter your Username: ");
        username = reader.next();
        System.out.println("Enter your Password: ");
        password = reader.next();
        
//        username = "drb56"; //This is your username in oracle
//        password = "Robert098$"; //This is your password in oracle
        
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
                connection.setAutoCommit(true);
                
                System.out.println("Testing createUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Users"));
                if(testCreateUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Users"));
                }
                
                System.out.println("Testing createGroup: \n\tNumber of rows before stress test: " + printNumRows(connection, "Groups"));
                if(testCreateGroup(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Groups"));
                }
                
                System.out.println("Testing initiateFriendship: \n\tNumber of rows before stress test: " + printNumRows(connection, "Friends"));
                if(testInitiateFriendship(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Friends"));
                }
//                System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Friends"));
                
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
                FaceSpace.User user = testSearchForUser(connection);
                if(user != null){
                    System.out.println("\tFinal user info: " + "\n\tName: " 
                            + user.getFname() + " " + user.getLname() 
                            + "\n\tUser ID: " + user.getUserID());
                }
                
                System.out.println("Testing displayFriends: \t");
                FaceSpace.Friendship friends = testDisplayFriends(connection);
                if(friends != null){
                    System.out.println("\tFinal friendship info: " + "\n\tFriendDate: " 
                            + friends.getFriendDate() + "\n\tFriendOne: " 
                            + friends.getFriendOne() + "\n\tFriendTwo: " + friends.getFriendTwo());
                }
                
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
//                                + dispArr[4] + dispArr[5] + dispArr[6] + dispArr[7] 
//                                + dispArr[8] + dispArr[9] + dispArr[10] + dispArr[11]);
                }
                
                System.out.println("Testing displayNewMessages:");
                String displayNewMessages = testDisplayNewMessages(connection);
                if(!top.equals("")){
                    String[] dispArr = displayNewMessages.split(" ");
                        System.out.println("\tFinal top message: \n" 
                                + dispArr[0] + dispArr[1] + dispArr[2] + dispArr[3] + dispArr[4]); 
//                                + dispArr[4] + dispArr[5] + dispArr[6] + dispArr[7] 
//                                + dispArr[8] + dispArr[9] + dispArr[10] + dispArr[11]);
                }
                
                System.out.println("Testing threeDegrees:");
                if(testThreeDegrees(connection)){
                    System.out.println("\tStress test succeeded!");
                }
                
                
                System.out.println("Testing dropUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Users"));
                if(testDropUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Users"));
                }
                
        }catch(Exception e){
            System.out.println("error connecting");
        }
    }
    
    
    
    public static boolean testDropUser(Connection connection) throws SQLException{
        for(int i = 1; i <= 3000; i++){
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
        for(int i = 1; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.sendMessageToGroup(connection, i, i, "blerg", "blahblah")){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testAddToGroup(Connection connection){
        for(int i = 1; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.addToGroup(connection, i, i)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testSendMessageToUser(Connection connection){
        for(int i = 1; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.sendMessageToUser(connection, "blahblah", "blerg", 9, 8)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testEstablishFriendship(Connection connection) throws SQLException{
        for(int i = 1; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.establishFriendship(connection, i)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testInitiateFriendship(Connection connection) throws ParseException, SQLException{
        for(int i = 2; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.initiateFriendship(connection, "2015-03-10", 0, i, 1)){
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
            if(FaceSpace.createGroup(connection, "blah", "test", 30)){
            }
            else{
                return false;
            }
        }
        return true;
    }

    public static FaceSpace.User testSearchForUser(Connection connection) throws SQLException, IllegalAccessException, ParseException{
        ArrayList<FaceSpace.User> results = null;
        for(int i = 0; i < 100; i++){
                //System.out.println("createGroup");
            results = FaceSpace.searchForUser(connection, "jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994");

            //print on the last iteration
            if (i == 3000-1){

//                System.out.println("The users found with the search string 'jim Omega Kent jones hello@yahoo.com dude 25 10-12-1994' were:");
                if (results.size() == 0){
//                    System.out.println("none");
                }
                for(int z = 0; z < results.size(); z++){
//                    System.out.println(results.get(z).getFname() + " " + results.get(z).getLname() + " " + results.get(z).getUserID());
                }
            }
        }
        return results.get(results.size()-1);
    }
    
    public static String testTopMessagers(Connection connection) throws SQLException{
        ArrayList<String> list = null;
        for(int i = 0; i <= 100; i++){
                //System.out.println("createUser");
            list = FaceSpace.topMessagers(connection, 10, "2015/01/01" );

        }
        return list.get(list.size()-1);
    }
    
    public static FaceSpace.Friendship testDisplayFriends(Connection connection) throws SQLException{
        ArrayList<FaceSpace.Friendship> friends = null;
        
        for(int i=500; i<=600; i++){
            friends = FaceSpace.displayFriends(connection, i);
        }
        return friends.get(friends.size()-1);
    }
    
    public static String testDisplayMessages(Connection connection) throws SQLException{
        ArrayList<String> list = null;
        for(int i = 0; i <= 100; i++){
                //System.out.println("createUser");
            list = FaceSpace.displayMessages(connection, 64);

        }
        return list.get(list.size()-1);
    }
    
    public static String testDisplayNewMessages(Connection connection) throws SQLException{
        ArrayList<String> list = null;
        for(int i = 0; i <= 100; i++){
                //System.out.println("createUser");
            list = FaceSpace.displayNewMessages(connection, 34);

        }
        return list.get(list.size()-1);
    }
    
    public static boolean testThreeDegrees(Connection connection) throws SQLException{
        ArrayList<Integer> list = null;
        for(int i = 0; i <= 100; i++){
                //System.out.println("createUser");
            list = FaceSpace.threeDegrees(connection, 3, 12 );

        }
        System.out.println("The middle ids between 3 and 12 are: ");
        for(int j = 0; j < list.size(); j++){
                System.out.println("\tID: " +list.get(j));
        }
        return list.size() != 0;
    }

}
