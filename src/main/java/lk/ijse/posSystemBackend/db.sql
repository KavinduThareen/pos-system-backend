create database name;
use pos_System;
create table customer(
                        id VARCHAR(50) primary key,
                        name VARCHAR(50),
                        address VARCHAR(50),
                        salory VARCHAR(20)

);

create table items(
                         code VARCHAR(50) primary key,
                         name VARCHAR(50),
                         qty VARCHAR(50),
                         price VARCHAR(20)

);

