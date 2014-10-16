drop database stack;
create database stack;
CREATE USER 'stack'@'localhost' IDENTIFIED BY 'stack';
GRANT ALL PRIVILEGES ON stack.* TO 'stack'@'localhost' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON test.* TO 'stack'@'localhost' WITH GRANT OPTION;
flush privileges;