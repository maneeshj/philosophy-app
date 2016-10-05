
-- Create database and set permissions to the following user
CREATE DATABASE philosophy DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT ALL ON philosophy.* TO 'maneesh'@'localhost' IDENTIFIED BY 'maneesh';

-- Create the required tables
-- id - autoincrementing unique identifier for every path/ transaction
-- source - source wikipedia URL/title entered
-- path - the total path taken to reach philosophy or until an error
-- hop_count - the amount of hops take to reach philosophy
-- timestamp - timestamp of the transaction
-- has_error - tells us whether an error occured for a transaction
-- error_code - if error occurs, gives out the error code which can be looked up at error_code table
CREATE TABLE philosophy.path (
   `id` int(11) NOT NULL AUTO_INCREMENT,
   `source` varchar(200) CHARACTER SET utf8 NOT NULL,
   `path` json DEFAULT NULL,
   `hop_count` int(11) NOT NULL DEFAULT '0',
   `timestamp` datetime DEFAULT NULL,
   `has_error` bit(1) NOT NULL,
   `error_code` int(11) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 
 -- List of all error codes are stored in this table
 CREATE TABLE philosophy.error_code (
   `code` int(11) NOT NULL,
   `name` varchar(45) COLLATE utf8_unicode_ci NOT NULL,
   PRIMARY KEY (`code`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
 
 
-- Populate Error code table
 INSERT INTO philosophy.error_code(code, name)
	VALUES (1, "Infinite loop"),
	 (2, "Connection timeout"),
	 (3, "Has invalid page")