create database if not exists bunge;


USE bunge;

GRANT ALL PRIVILEGES ON bunge.* TO 'lanacondio'@'localhost';

SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

/*Table structure for table `test` */

CREATE TABLE IF NOT EXISTS user (
   id int(11) NOT NULL auto_increment,   
   file varchar(100) NOT NULL default '0',       
   name varchar(100) NOT NULL default '0',       
   laboral_base varchar(100)  NOT NULL default '',     
   last_login  DATETIME  NULL,       
   password varchar(100) not null,
   email varchar(100) null,
   PRIMARY KEY  (id)
);

CREATE TABLE IF NOT EXISTS category (
   id int(11) NOT NULL auto_increment,   
   description varchar(250) NOT NULL default '',          
   PRIMARY KEY  (id)
);

CREATE TABLE IF NOT EXISTS product (
   id int(11) NOT NULL auto_increment,   
   category_id  int(11) NOT NULL,       
   description varchar(500) NOT NULL default '',       
   photo_url varchar(500) NOT NULL default '',       
   stock  int(11) NOT NULL,       
   PRIMARY KEY  (id),
   FOREIGN KEY (category_id) REFERENCES category(id)
);


CREATE TABLE IF NOT EXISTS credit (
   id int(11) NOT NULL auto_increment,   
   user_id int(11) NOT NULL,       
   category_id int(11) NOT NULL,       
   quantity int(11) NOT NULL default 0,
   PRIMARY KEY  (id),
   FOREIGN KEY (category_id) REFERENCES category(id),
   FOREIGN KEY (user_id) REFERENCES user(id)   
);


CREATE TABLE IF NOT EXISTS transaction (
   id int(11) NOT NULL auto_increment,   
   date  DATETIME  NULL,       
   product_id int(11) NOT NULL,       
   user_id int(11) NOT NULL,          
   PRIMARY KEY  (id),
   FOREIGN KEY (product_id) REFERENCES product(id),
   FOREIGN KEY (user_id) REFERENCES user(id)   
);
