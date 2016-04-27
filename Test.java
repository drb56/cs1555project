
//import static FaceSpace;
import java.sql.*;
import java.text.ParseException;
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
                
                System.out.println("Testing dropUser: \n\tNumber of rows before stress test: " + printNumRows(connection, "Users"));
                if(testDropUser(connection)){
                    System.out.println("\tNumber of rows after stress test: " + printNumRows(connection, "Users"));
                }
                
        }catch(Exception e){
            System.out.println("error connecting");
        }
    }
    
    public static boolean testDropUser(Connection connection){
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
            if(FaceSpace.establishFriendship(connection, 201)){
            }
            else{
                return false;
            }
        }
        return true;
    }
    
    public static boolean testInitiateFriendship(Connection connection) throws ParseException{
        for(int i = 1; i <= 3000; i++){
                                //System.out.println("createUser");
            if(FaceSpace.initiateFriendship(connection, "2015-03-10", 0, 8, 9)){
            }
            else{
                return false;
            }
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
    
    public static boolean testCreateUser(Connection connection){
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
}
