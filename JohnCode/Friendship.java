

public class Friendship{

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