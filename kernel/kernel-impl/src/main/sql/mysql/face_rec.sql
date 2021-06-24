#say for which database you want to add table:
USE sakaidatabase;

#create table FACE_REC:
CREATE TABLE FACE_REC
(
       ID                   BIGINT NOT NULL AUTO_INCREMENT,
       USER_ID              VARCHAR (99) NOT NULL,
       PHOTO                BLOB,
       primary key (ID)
);
# we need to add values from what already exists in SAKAI_DATABASE,
# step 1 - migrate data
# step 2 - add constraint
INSERT INTO sakaidatabase.FACE_REC(USER_ID) SELECT USER_ID  FROM sakaidatabase.SAKAI_USER;


#alter table SAKAI_HELP_RESOURCE add constraint FKC23F5132DBFCB7FC foreign key (CATEGORY_ID) references SAKAI_HELP_CATEGORY
ALTER TABLE FACE_REC ADD CONSTRAINT FACE_LINK_CONSTRAINT FOREIGN KEY (USER_ID) REFERENCES SAKAI_USER(USER_ID);

# alter USER_SAKAI to add a new boolean to say if user has pressed for FACE_REC
ALTER TABLE SAKAI_USER ADD COLUMN  ISFACEENABLED BOOLEAN DEFAULT FALSE;



#LITERALLY COMENZILE FOLOSITE IN MYSQL:
#USE sakaidatabase;
#CREATE TABLE FACE_REC
#(
#    ID                   BIGINT NOT NULL AUTO_INCREMENT,
#    USER_ID              VARCHAR (99) NOT NULL,
#    PHOTO                BLOB,
#    primary key (ID)
#);
#
#ALTER TABLE SAKAI_USER ADD COLUMN  ISFACEENABLED BOOLEAN DEFAULT FALSE;
#
#INSERT INTO sakaidatabase.FACE_REC(USER_ID) SELECT USER_ID  FROM sakaidatabase.SAKAI_USER;
#
#ALTER TABLE  sakaidatabase.FACE_REC ADD CONSTRAINT FACE_LINK_CONSTRAINT FOREIGN KEY (USER_ID) REFERENCES SAKAI_USER(USER_ID);