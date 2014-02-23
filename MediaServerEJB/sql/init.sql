DROP DATABASE IF EXISTS bandplan;
DROP USER bandplan@localhost;
CREATE USER bandplan@localhost identified by 'bandpl4n';
CREATE DATABASE bandplan;
GRANT ALL PRIVILEGES ON bandplan.* TO bandplan@localhost;
USE bandplan;
DELIMITER //
CREATE DEFINER=`bandplan`@`localhost` PROCEDURE `geodist`(IN mylat double, IN mylon double, IN dist int, IN pageNum int, IN pageCount int)
BEGIN
declare lon1 float;
declare lon2 float;
declare lat1 float;
declare lat2 float;

declare offset int;
set offset = pageCount * (pageNum-1);

set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
set lat1 = mylat-(dist/69);
set lat2 = mylat+(dist/69);

SELECT distinct dest.*,
3956 * 2 * ASIN(SQRT( POWER(SIN((mylat -dest.latitude) * pi()/180 / 2), 2) +COS(mylat * pi()/180) * COS(dest.latitude * pi()/180) *POWER(SIN((mylon-dest.longitude) * pi()/180 / 2), 2) )) as distance
FROM Repeater dest, Repeater orig
WHERE dest.longitude between lon1 and lon2
and dest.latitude between lat1 and lat2 
HAVING distance < dist
ORDER BY distance;
END//
DELIMITER ;
