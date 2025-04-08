drop database if exists `my_jdbc`;
create database `my_jdbc`;

drop table if exists `member`;
create table `member` (
    `member_id` int not null primary key auto_increment,
    `username` varchar(100) not null,
    `password` varchar(100) not null
);