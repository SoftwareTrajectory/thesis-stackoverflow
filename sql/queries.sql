select pss.* from `preseries_sax` pss 
where pss.seriesId in (select id from preseries where userid in
 (select id from users where locationId in (select id from locations where tzname="Asia/Kolkata")))
and pss.sax_string="aaac";

alter table preseries_sax add KEY `preseries_sax_word` (`sax_string`) using HASH;

select count(*) as `count`, pss.sax_string as sax from `preseries_sax` pss 
where pss.seriesId in (select id from preseries where userid in
 (select id from users where locationId in (select id from locations where tzname="America/New_York")))
group by sax order by `count` desc;

select count(*) as `count`, pss.sax_string as sax from `preseries_sax` pss 
where pss.seriesId in (select id from preseries where userid in
 (select id from users where locationId in (select id from locations where tzname="Asia/Kolkata")))
group by sax order by `count` desc;

--
--
--


select count(distinct(seriesId)) from `preseries_sax`;

select * from preseries where `userid`=1;

alter table preseries_event add KEY `preseries_event_series` (`seriesId`);

drop table preseries_sax;
CREATE TABLE `preseries_sax` (
    `seriesId` int(11) NOT NULL,
    `date` date NOT NULL,
    `sax_string` character(5) NOT NULL
    ) ENGINE=MyISAM DEFAULT CHARSET=latin1;

		SELECT pe.* from preseries_event pe join
		preseries ps on
		pe.seriesId=ps.id where ps.userId=1
		order by pe.date asc limit 1;

alter table preseries_event add key pre_event_date(date asc);


explain(		SELECT * from users
		where id in (select distinct(userId) from preseries));

select * from locations where tzname like "%London%";

-- 2	A	2009-11-23	12	2

select * from `preseries_event` where `tag`='C';

select * from `preseries` where id=2;

select * from `preseries_event` where `seriesId`=2 order by `date` asc;

select * from postseries where `ownerUserId`=184 order by `creationDate` asc;



drop TABLE `preseries_event`;
CREATE TABLE `preseries_event` (
  `seriesId` int(11) NOT NULL,
  `tag` char(1) NOT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  `hour` smallint(6) NOT NULL,
  `counter` smallint(6) NOT NULL,
 unique key preseries_event(seriesId, tag, date, hour)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

drop table preseries;
CREATE TABLE `preseries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_series` (`userid`,`tag`),
  KEY `preseries_user` (`userid`),
  KEY `preseries_tag` (`tag`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;