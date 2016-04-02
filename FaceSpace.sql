DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE members CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;
PURGE RECYCLEBIN;

CREATE TABLE users(
    userID     		NUMBER(10),
    fname      		VARCHAR2(32),
    lname      		VARCHAR2(32),
    email      		VARCHAR2(32),
    DOB        		DATE,
    lastLogin     TIMESTAMP,
    PRIMARY KEY(userID)
);
		
CREATE TABLE friends(
		friendID			NUMBER(10),
		friendDate		TIMESTAMP,
		friendStatus	NUMBER(1),
		userID1				NUMBER(10),
		userID2				NUMBER(10),
		PRIMARY KEY(friendID),
		FOREIGN KEY(userID1) REFERENCES users(userID),
		FOREIGN KEY(userID2) REFERENCES users(userID)
);

CREATE TABLE groups(
		groupID				NUMBER(10),
		name					VARCHAR2(32),
		description		VARCHAR2(32),
		personLimit		NUMBER(10),
		PRIMARY KEY(groupID)
);

CREATE TABLE members(
		groupID				NUMBER(10),
		userID				NUMBER(10),
		PRIMARY KEY(groupID, userID),
		FOREIGN KEY(groupID) REFERENCES groups(groupID),
		FOREIGN KEY(userID) REFERENCES users(userID)
);

CREATE TABLE messages(
		msgID			NUMBER(10),
		subject		VARCHAR2(32),
		msgText		VARCHAR2(1024),
		dateSent	TIMESTAMP,
		senderID	NUMBER(10),
		recipientGroupID		NUMBER(10),
		recipientUserID			NUMBER(10),
		PRIMARY KEY(msgID),
		FOREIGN KEY(senderID) REFERENCES users(userID),
		FOREIGN KEY(recipientGroupID) REFERENCES groups(groupID),
		FOREIGN KEY(recipientUserID) REFERENCES users(userID)
);