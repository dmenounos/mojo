# create user 'mojo'@'localhost' identified by 'qweasd';
# grant all on mojo.* to 'mojo'@'localhost';

drop database if exists mojo;
create database mojo;
use mojo;

create table mojo_node (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	AuditableEntity
	createDate          datetime,
	createUser_id       int,
	updateDate          datetime,
	updateUser_id       int,
--	AbstractRule
	permissions         int not null,
--	Node
	code                varchar(255) not null,
	action              varchar(255),
	parentNode_id       int,
	terminal            bit not null,
	hidden              bit not null
) engine=InnoDB;

create table mojo_node_rule (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	AuditableEntity
	createDate          datetime,
	createUser_id       int,
	updateDate          datetime,
	updateUser_id       int,
--	AbstractRule
	permissions         int not null,
--	NodeRule
	pos                 int not null,
	node_id             int not null,
	group_id            int not null
) engine=InnoDB;

create table mojo_post (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	AuditableEntity
	createDate          datetime,
	createUser_id       int,
	updateDate          datetime,
	updateUser_id       int,
--	Post
	title               varchar(255) not null,
	content             text,
	parentNode_id       int,
	terminal            bit not null,
	hidden              bit not null
) engine=InnoDB;

create table mojo_country (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	Country
	code                varchar(255) not null,
	name                varchar(255) not null
) engine=InnoDB;

create table mojo_language (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	Language
	code                varchar(255) not null,
	name                varchar(255) not null
) engine=InnoDB;

create table mojo_openid (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	OpenID
	user_id             int not null,
	address             varchar(255) not null
) engine=InnoDB;

create table mojo_user (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	User
	email               varchar(255) not null,
	nickname            varchar(255) not null,
	password            varchar(255) not null,
	fullname            varchar(255),
	gender              varchar(255),
	birthday            date,
	country_id          int,
	language_id         int,
	postcode            varchar(255),
	timezone            varchar(255),
	signUpTime          datetime,
	signInTime          datetime
) engine=InnoDB;

create table mojo_user_group (
--	AbstractEntity
	id                  int not null auto_increment primary key,
--	UserGroup
	name                varchar(255) not null
) engine=InnoDB;

create table mojo_user_group_assoc (
	user_id             int not null,
	group_id            int not null
) engine=InnoDB;

create table mojo_audits (
	entityClass         varchar(255) not null,
	entityId            int not null,
	createUser          varchar(255),
	createDate          datetime,
	updateUser          varchar(255),
	updateDate          datetime,
    primary key (entityClass, entityId)
) engine=InnoDB;

alter table mojo_node add index (createUser_id);
alter table mojo_node add foreign key (createUser_id) references mojo_user(id);
alter table mojo_node add index (updateUser_id);
alter table mojo_node add foreign key (updateUser_id) references mojo_user(id);
alter table mojo_node add index (parentNode_id);
alter table mojo_node add foreign key (parentNode_id) references mojo_node(id) on delete cascade;
alter table mojo_node add unique key (parentNode_id, code);

alter table mojo_node_rule add index (createUser_id);
alter table mojo_node_rule add foreign key (createUser_id) references mojo_user(id);
alter table mojo_node_rule add index (updateUser_id);
alter table mojo_node_rule add foreign key (updateUser_id) references mojo_user(id);
alter table mojo_node_rule add index (node_id);
alter table mojo_node_rule add foreign key (node_id) references mojo_node(id) on delete cascade;
alter table mojo_node_rule add index (group_id);
alter table mojo_node_rule add foreign key (group_id) references mojo_user_group(id) on delete cascade;
alter table mojo_node_rule add unique key (node_id, group_id);
alter table mojo_node_rule add unique key (node_id, pos);

alter table mojo_post add index (createUser_id);
alter table mojo_post add foreign key (createUser_id) references mojo_user(id);
alter table mojo_post add index (updateUser_id);
alter table mojo_post add foreign key (updateUser_id) references mojo_user(id);
alter table mojo_post add index (parentNode_id);
alter table mojo_post add foreign key (parentNode_id) references mojo_node(id) on delete cascade;

alter table mojo_openid add index (user_id);
alter table mojo_openid add foreign key (user_id) references mojo_user(id) on delete cascade;
alter table mojo_openid add unique key (address);

alter table mojo_user add index (country_id);
alter table mojo_user add foreign key (country_id) references mojo_country(id);
alter table mojo_user add index (language_id);
alter table mojo_user add foreign key (language_id) references mojo_language(id);

alter table mojo_user_group_assoc add index (user_id);
alter table mojo_user_group_assoc add foreign key (user_id) references mojo_user(id) on delete cascade;
alter table mojo_user_group_assoc add index (group_id);
alter table mojo_user_group_assoc add foreign key (group_id) references mojo_user_group(id) on delete cascade;
alter table mojo_user_group_assoc add unique key (user_id, group_id);
