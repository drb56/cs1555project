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
public class Friendship{

	private String friendDate;
	private int friend1;
	private int friend2;
	private boolean friendStatus;

	public Friendship(String date, int friend1, int friend2, boolean status){
			this.friendDate = date;
			this.friend1 = friend1;
			this.friend2 = friend2;
			this.friendStatus = status;
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
