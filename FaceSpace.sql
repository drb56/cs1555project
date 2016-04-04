DROP TABLE users CASCADE CONSTRAINTS;
DROP TABLE friends CASCADE CONSTRAINTS;
DROP TABLE groups CASCADE CONSTRAINTS;
DROP TABLE members CASCADE CONSTRAINTS;
DROP TABLE messages CASCADE CONSTRAINTS;
DROP SEQUENCE users_seq;
DROP SEQUENCE friends_seq;
DROP SEQUENCE groups_seq;
DROP SEQUENCE messages_seq;
PURGE RECYCLEBIN;

--CREATE USERS TABLE
CREATE TABLE Users(
	fname 						VARCHAR2(32) NOT NULL,
	lname 						VARCHAR2(32) NOT NULL,
	email 						VARCHAR2(32) NOT NULL,
	dateOfBirth					DATE NOT NULL,
	lastLogin 					TIMESTAMP NOT NULL,
	userID 						NUMBER(10),
	PRIMARY KEY(userID)
);

--USERS SEQUENCE
CREATE SEQUENCE users_seq;

--USERS ID AUTOINCREMENT
CREATE OR REPLACE TRIGGER users_increment 
BEFORE INSERT ON Users 
FOR EACH ROW

BEGIN
  SELECT users_seq.NEXTVAL
  INTO   :new.userID
  FROM   dual;
END;
/

--CREATE FRIENDS TABLE
CREATE TABLE Friends(
		friendDate 				TIMESTAMP NOT NULL,
		friendStatus 			NUMBER(1) NOT NULL,
		userID1 				NUMBER(10) NOT NULL,
		userID2 				NUMBER(10) NOT NULL,
		friendID 				NUMBER(10),
		PRIMARY KEY(friendID),
		FOREIGN KEY(userID1) REFERENCES users(userID),
		FOREIGN KEY(userID2) REFERENCES users(userID)
);

--FRIENDS SEQUENCE
CREATE SEQUENCE friends_seq;

--FRIENDS ID AUTOINCREMENT
CREATE OR REPLACE TRIGGER friends_increment 
BEFORE INSERT ON Friends 
FOR EACH ROW

BEGIN
  SELECT friends_seq.NEXTVAL
  INTO   :new.friendID
  FROM   dual;
END;
/

--CREATE GROUPS TABLE
CREATE TABLE Groups(
		name 					VARCHAR2(32) NOT NULL,
		description 			VARCHAR2(32) NOT NULL,
		personLimit 			NUMBER(10) NOT NULL,
		groupID 				NUMBER(10),
		PRIMARY KEY(groupID)
);

--GROUPS SEQUENCE
CREATE SEQUENCE groups_seq;

--GROUPS ID AUTOINCREMENT
CREATE OR REPLACE TRIGGER groups_increment 
BEFORE INSERT ON Groups 
FOR EACH ROW

BEGIN
  SELECT groups_seq.NEXTVAL
  INTO   :new.groupID
  FROM   dual;
END;
/

--CREATE MEMBERS TABLE
CREATE TABLE Members(
		groupID 				NUMBER(10),
		userID 					NUMBER(10),
		PRIMARY KEY(groupID, userID),
		FOREIGN KEY(groupID) REFERENCES groups(groupID),
		FOREIGN KEY(userID) REFERENCES users(userID)
);

--CREATE MESSAGES TABLE
CREATE TABLE Messages(
		subject 				VARCHAR2(32),
		msgText 				VARCHAR2(1024),
		dateSent 				TIMESTAMP,
		senderID 				NUMBER(10),
		recipientID 			NUMBER(10),
		msgID 					NUMBER(10),
		PRIMARY KEY(msgID),
		FOREIGN KEY(senderID) REFERENCES users(userID),
		FOREIGN KEY(recipientID) REFERENCES users(userID)
);

--MESSAGES SEQUENCE
CREATE SEQUENCE messages_seq;

--MESSAGES ID AUTOINCREMENT
CREATE OR REPLACE TRIGGER messages_increment 
BEFORE INSERT ON Messages 
FOR EACH ROW

BEGIN
  SELECT messages_seq.NEXTVAL
  INTO   :new.msgID
  FROM   dual;
END;
/