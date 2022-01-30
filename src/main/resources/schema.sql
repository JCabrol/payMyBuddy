
CREATE TABLE if not exists Person (
                mail VARCHAR(100) NOT NULL,
                password VARCHAR(100) NOT NULL,
                firstName VARCHAR(100) NOT NULL,
                lastName VARCHAR(100) NOT NULL,
                availableBalance FLOAT DEFAULT 0 NOT NULL,
                PRIMARY KEY (mail)
);


CREATE TABLE if not exists BankAccount (
                iban VARCHAR(34) NOT NULL,
                ownerId VARCHAR(100) NOT NULL,
                bic VARCHAR(11) NOT NULL,
                PRIMARY KEY (iban)
);


CREATE TABLE if not exists TransactionWithBank (
                transactionBankId INT AUTO_INCREMENT NOT NULL,
                amount FLOAT NOT NULL,
                senderId VARCHAR(100) NOT NULL,
                receiverId VARCHAR(34) NOT NULL,
                date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                description VARCHAR(100),
                PRIMARY KEY (transactionBankId)
);


CREATE TABLE if not exists TransactionBetweenPersons (
                transactionPersonId INT AUTO_INCREMENT NOT NULL,
                amount FLOAT NOT NULL,
                senderId VARCHAR(100) NOT NULL,
                receiverId VARCHAR(100) NOT NULL,
                date DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                description VARCHAR(100),
                PRIMARY KEY (transactionPersonId)
);


CREATE TABLE if not exists Relations (
                relationInstigator VARCHAR(100) NOT NULL,
                relationReceiver VARCHAR(100) NOT NULL,
                PRIMARY KEY (relationInstigator, relationReceiver)
);


ALTER TABLE Relations ADD CONSTRAINT relations_person_fk
FOREIGN KEY (relationReceiver)
REFERENCES Person (mail);

ALTER TABLE Relations ADD CONSTRAINT person_relations_fk
FOREIGN KEY (relationInstigator)
REFERENCES Person (mail) ;

ALTER TABLE TransactionBetweenPersons ADD CONSTRAINT person_transactionBetweenPersons_fk
FOREIGN KEY (senderId)
REFERENCES Person (mail);

ALTER TABLE TransactionBetweenPersons ADD CONSTRAINT person_transactionBetweenPersons_fk1
FOREIGN KEY (receiverId)
REFERENCES Person (mail);

ALTER TABLE TransactionWithBank ADD CONSTRAINT person_transactionWithBank_fk
FOREIGN KEY (senderId)
REFERENCES Person (mail);

ALTER TABLE BankAccount ADD CONSTRAINT person_bankAccount_fk
FOREIGN KEY (ownerId)
REFERENCES Person (mail);

ALTER TABLE TransactionWithBank ADD CONSTRAINT bankAccount_transactionWithBank_fk
FOREIGN KEY (receiverId)
REFERENCES BankAccount (iban);