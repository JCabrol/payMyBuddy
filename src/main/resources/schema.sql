
CREATE DATABASE PayMyBuddy;

USE PayMyBuddy;

CREATE TABLE Person (
                email VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL,
                first_Name VARCHAR(100) NOT NULL,
                last_Name VARCHAR(100) NOT NULL,
                available_Balance FLOAT DEFAULT 0 NOT NULL,
				active BOOLEAN DEFAULT TRUE,
				role VARCHAR(10) NOT NULL,
                PRIMARY KEY (email)
);


CREATE TABLE Bank_Account (
                iban VARCHAR(34) NOT NULL,
                owner_Id VARCHAR(100) NOT NULL,
                bic VARCHAR(11) NOT NULL,
				usual_Name VARCHAR(100),
				active_Bank_Account BOOLEAN DEFAULT TRUE,
                PRIMARY KEY (iban)
);


CREATE TABLE Transaction_With_Bank (
                transaction_Id INT AUTO_INCREMENT NOT NULL,
                amount FLOAT NOT NULL,
                sender_Id VARCHAR(100) NOT NULL,
                receiver_Id VARCHAR(34) NOT NULL,
                date_Time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                description VARCHAR(100),
                PRIMARY KEY (transaction_Id)
);


CREATE TABLE Transaction_Between_Persons (
                transaction_Id INT AUTO_INCREMENT NOT NULL,
                amount FLOAT NOT NULL,
                sender_Id VARCHAR(100) NOT NULL,
                receiver_Id VARCHAR(100) NOT NULL,
                date_Time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                description VARCHAR(100),
                PRIMARY KEY (transaction_Id)
);


CREATE TABLE Relations (
                relation_Instigator VARCHAR(100) NOT NULL,
                relation_Receiver VARCHAR(100) NOT NULL,
                PRIMARY KEY (relation_Instigator, relation_Receiver)
);

CREATE TABLE Message_To_Admin (
                message_Id INT AUTO_INCREMENT NOT NULL,
				subject VARCHAR(80) NOT NULL,
				message VARCHAR(500) NOT NULL,
				email VARCHAR(100) NOT NULL,
                first_Name VARCHAR(100) ,
                last_Name VARCHAR(100) ,
				new_Message BOOLEAN DEFAULT TRUE,
				date_Time DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                PRIMARY KEY (message_Id)
);

ALTER TABLE Relations ADD CONSTRAINT relations_person_fk
FOREIGN KEY (relation_Receiver)
REFERENCES Person (email);

ALTER TABLE Relations ADD CONSTRAINT person_relations_fk
FOREIGN KEY (relation_Instigator)
REFERENCES Person (email);

ALTER TABLE Transaction_Between_Persons ADD CONSTRAINT person_transaction_between_persons_fk
FOREIGN KEY (sender_Id)
REFERENCES Person (email);

ALTER TABLE Transaction_Between_Persons ADD CONSTRAINT person_transaction_between_persons_fk1
FOREIGN KEY (receiver_Id)
REFERENCES Person (email);

ALTER TABLE Transaction_With_Bank ADD CONSTRAINT person_transaction_with_bank_fk
FOREIGN KEY (sender_Id)
REFERENCES Person (email);

ALTER TABLE Bank_Account ADD CONSTRAINT person_bank_account_fk
FOREIGN KEY (owner_Id)
REFERENCES Person (email);

ALTER TABLE Transaction_With_Bank ADD CONSTRAINT bank_account_transaction_with_bank_fk
FOREIGN KEY (receiver_Id)
REFERENCES Bank_Account (iban);
