-- setup for EARS database

CREATE TABLE IF NOT EXISTS UserAccounts(
	First_Name 			VARCHAR(30),
    Last_Name 			VARCHAR(30),
    Email 				VARCHAR(50),
    Pword 				VARCHAR(50),
    UserID  			INT AUTO_INCREMENT,
    Title  				VARCHAR(50),
    Account_Type  		VARCHAR(30),
    CONSTRAINT pk_users PRIMARY KEY (userID)
);
    
CREATE TABLE IF NOT EXISTS FacultySearches(
	Start_Date 			DATE,
    End_Date 			DATE,
    Search_Position 	VARCHAR(50),
    Search_Description 	VARCHAR(1000),
    Requirements 		VARCHAR(1000),
    SearchID 			INT AUTO_INCREMENT,
    CONSTRAINT pk_searches PRIMARY KEY (searchID)
);

CREATE TABLE IF NOT EXISTS SearchApplicants(
	First_Name 			VARCHAR(30),
    Last_Name 			VARCHAR(30),
    Link_To_Resume 		VARCHAR(30),
    ApplicantID 		INT AUTO_INCREMENT,
    CONSTRAINT pk_applicants PRIMARY KEY (ApplicantID)
);

CREATE TABLE IF NOT EXISTS CommitteeAssignments(
	SearchID 			INT,
    UserID 				INT,
    Committee_Rank 		VARCHAR(30),
    CONSTRAINT pk_committee_assignments PRIMARY KEY (searchID, userID),
    CONSTRAINT fk_committee_user FOREIGN KEY (userID)
		REFERENCES UserAccounts (userID),
	CONSTRAINT fk_committee_search FOREIGN KEY (searchID)
		REFERENCES FacultySearches (searchID)
);

CREATE TABLE IF NOT EXISTS ApplicantAssignments(
	SearchID 			INT,
    ApplicantID 		INT,
    Review_Completed 	BOOLEAN,
    Applicant_Status 	VARCHAR(30),
    CONSTRAINT pk_applicant_assignments PRIMARY KEY (SearchID, ApplicantID),
	CONSTRAINT fk_app_assignments_search FOREIGN KEY (SearchID)
		REFERENCES FacultySearches (SearchID),
	CONSTRAINT fk_app_assignments_applicants FOREIGN KEY (ApplicantID)
		REFERENCES SearchApplicants (ApplicantID)
);

CREATE TABLE IF NOT EXISTS Comments(
	UserID 				INT,
    ApplicantID 		INT,
    Comment_Date 		DATE,
    Comment_Text 		VARCHAR(1000),
    CommentID			INT AUTO_INCREMENT,
	CONSTRAINT pk_comments PRIMARY KEY (CommentID),
    CONSTRAINT fk_comment_user FOREIGN KEY (userID)
		REFERENCES UserAccounts (userID),
	CONSTRAINT fk_comment_applicants FOREIGN KEY (ApplicantID)
		REFERENCES SearchApplicants (ApplicantID)
);

INSERT IGNORE INTO UserAccounts (Email, Pword, Account_Type, UserID)
	VALUES ("admin", "admin", "administrator", 1)