

import java.sql.*;  //import the file containing definitions for the parts
import java.util.Scanner;
import java.io.File;
import java.text.ParseException;
import java.io.FileNotFoundException;
import oracle.jdbc.*; //needed by java for database connection and manipulation



public class ResetDB {
    private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; // used to hold the result of your query (if one exists)
    private String query;  //this will hold the query we are using



    private static void runArrayOfStatements(Connection conn, String[] statements, Boolean printErrors){
        Statement currentStatement = null;
        for(String rawStatement : statements){
            try{
                System.out.println("executing: " + rawStatement);
                currentStatement = conn.createStatement();
                currentStatement.execute(rawStatement);
            }
            catch (SQLException e){
                if(printErrors){
                    e.printStackTrace();
                }
            }
            finally{
                //release resources
                if (currentStatement != null){
                    try{
                        currentStatement.close();
                    }
                    catch(SQLException e){
                        if(printErrors){
                            e.printStackTrace();
                        }
                    }
                }
                currentStatement = null;
            }
        }
    }


    public static void reset(Connection conn){
        Statement currentStatement = null;

        //DROP OLD TABLES AND SEQUENCES
        String[] resets = new String[]{ 
            "DROP TABLE users CASCADE CONSTRAINTS",
            "DROP TABLE friends CASCADE CONSTRAINTS",
            "DROP TABLE groups CASCADE CONSTRAINTS",
            "DROP TABLE members CASCADE CONSTRAINTS",
            "DROP TABLE messages CASCADE CONSTRAINTS",
            "DROP SEQUENCE users_seq",
            "DROP SEQUENCE friends_seq",
            "DROP SEQUENCE groups_seq",
            "DROP SEQUENCE messages_seq",
            "PURGE RECYCLEBIN"
        };

        //CREATE THE SEQUENCES
        String[] createSequences = new String[]{
            "CREATE SEQUENCE users_seq",
            "CREATE SEQUENCE friends_seq",
            "CREATE SEQUENCE groups_seq",
            "CREATE SEQUENCE messages_seq"
        };

        //CREATE THE TABLES
        String[] createTables = new String[]{
            "CREATE TABLE Users(fname VARCHAR2(32) NOT NULL, lname VARCHAR2(32) NOT NULL, email VARCHAR2(32) NOT NULL, dateOfBirth DATE NOT NULL, lastLogin TIMESTAMP NOT NULL, userID NUMBER(10), PRIMARY KEY(userID))",
            "CREATE TABLE Friends(friendDate TIMESTAMP NOT NULL, friendStatus NUMBER(1) NOT NULL, userID1 NUMBER(10) NOT NULL, userID2 NUMBER(10) NOT NULL, friendID NUMBER(10), PRIMARY KEY(friendID), FOREIGN KEY(userID1) REFERENCES users(userID), FOREIGN KEY(userID2) REFERENCES users(userID))",
            "CREATE TABLE Groups(name VARCHAR2(32) NOT NULL, description VARCHAR2(32) NOT NULL, personLimit NUMBER(10) NOT NULL, groupID NUMBER(10), PRIMARY KEY(groupID))",
            "CREATE TABLE Members(groupID NUMBER(10), userID NUMBER(10), PRIMARY KEY(groupID, userID), FOREIGN KEY(groupID) REFERENCES groups(groupID), FOREIGN KEY(userID) REFERENCES users(userID))",
            "CREATE TABLE Messages(subject VARCHAR2(32), msgText VARCHAR2(1024), dateSent TIMESTAMP, senderID NUMBER(10), recipientID NUMBER(10), msgID NUMBER(10), PRIMARY KEY(msgID), FOREIGN KEY(senderID) REFERENCES users(userID), FOREIGN KEY(recipientID) REFERENCES users(userID))"
        };


        String users_increment = ""+
            "CREATE OR REPLACE TRIGGER users_increment \n" + 
            "BEFORE INSERT ON Users \n" + 
            "FOR EACH ROW \n" + 
            "BEGIN \n" + 
            "SELECT users_seq.NEXTVAL INTO :new.userID FROM dual; \n" + 
            "END";

        String friends_increment = ""+
            "CREATE OR REPLACE TRIGGER friends_increment \n" + 
            "BEFORE INSERT ON Friends \n" + 
            "FOR EACH ROW \n" + 
            "BEGIN \n" + 
            "SELECT friends_seq.NEXTVAL INTO :new.friendID FROM dual; \n" + 
            "END";

        String groups_increment = ""+
            "CREATE OR REPLACE TRIGGER groups_increment \n" + 
            "BEFORE INSERT ON Groups \n" + 
            "FOR EACH ROW \n" + 
            "BEGIN \n" + 
            "SELECT groups_seq.NEXTVAL INTO :new.groupID FROM dual; \n" + 
            "END";

        String messages_increment = ""+
            "CREATE OR REPLACE TRIGGER messages_increment \n" + 
            "BEFORE INSERT ON Messages \n" + 
            "FOR EACH ROW \n" + 
            "BEGIN \n" + 
            "SELECT messages_seq.NEXTVAL INTO :new.msgID FROM dual; \n" + 
            "END";


        //CREATE THE TRIGGERS
        String[] createTriggers = new String[]{
            users_increment, friends_increment, groups_increment, messages_increment
        };

        runArrayOfStatements(conn, resets, true);
        runArrayOfStatements(conn, createTables, true);
        runArrayOfStatements(conn, createSequences, true);
        runArrayOfStatements(conn, createTriggers, true);

    }
        
    public static void main(String args[]) throws SQLException {
        /* Making a connection to a DB causes certain exceptions.  In order to handle
           these, you either put the DB stuff in a try block or have your function
           throw the Exceptions and handle them later. */

        System.setProperty( "oracle.jdbc.Trace", Boolean.TRUE.toString() );

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
            connection.setAutoCommit(false);
            reset(connection);
            connection.commit();
            
            
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