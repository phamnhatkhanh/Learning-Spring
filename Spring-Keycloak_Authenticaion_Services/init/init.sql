CREATE DATABASE IF NOT EXISTS `springbootadvertisement`;
CREATE DATABASE IF NOT EXISTS `springbootreport`;
CREATE DATABASE IF NOT EXISTS `springbootuser`;

-- CREATE USER 'root'@'localhost' IDENTIFIED BY 'local';
-- GRANT ALL ON *.* TO 'root'@'%';

-- GRANT ALL PRIVILEGES ON `springbootadvertisement`.* TO 'root'@'%' WITH GRANT OPTION;
-- GRANT ALL PRIVILEGES ON `springbootreport`.* TO 'root'@'%' WITH GRANT OPTION;
-- GRANT ALL PRIVILEGES ON `springbootuser`.* TO 'root'@'%' WITH GRANT OPTION;

GRANT ALL ON `springbootadvertisement`.* TO 'root'@'%';
GRANT ALL ON `springbootreport`.* TO 'root'@'%';
GRANT ALL ON `springbootuser`.* TO 'root'@'%';