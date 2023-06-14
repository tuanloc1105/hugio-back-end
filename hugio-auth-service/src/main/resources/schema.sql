drop table if exists USERS;
create table USERS
(
    ID         bigint       not null auto_increment,
    ACTIVE     bit          not null,
    CREATED_AT datetime(6) not null,
    CREATED_BY varchar(255),
    UPDATED_AT datetime(6) not null,
    UPDATED_BY varchar(255),
    PASSWORD   varchar(255) not null,
    USER_UID   binary(16) not null,
    USERNAME   varchar(255) not null,
    primary key (ID)
) engine=InnoDB;

