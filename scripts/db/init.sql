CREATE SCHEMA `accounts` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
CREATE USER `accounts`@`%` IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON accounts.* TO `accounts`@`%` IDENTIFIED BY 'password' WITH GRANT OPTION;
