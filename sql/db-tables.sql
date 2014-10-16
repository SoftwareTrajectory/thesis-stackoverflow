CREATE TABLE `badges` (
  `id` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `name` char(25) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `badges_user_key` (`userId`),
  KEY `badges_name_key` (`name`),
  KEY `badges_date_key` (`date`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `comments` (
  `id` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `text` text CHARACTER SET utf8 COLLATE utf8_bin,
  `score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `commentseries` (
  `id` int(11) NOT NULL,
  `postId` int(6) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_owner_key` (`userId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `locations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `location` varchar(128) DEFAULT NULL,
  `normalized_location` varchar(96) DEFAULT NULL,
  `known_location` varchar(18000) DEFAULT NULL,
  `tzname` varchar(64) DEFAULT NULL,
  `gmtoffset` varchar(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `normalized_location` (`normalized_location`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `posthistory` (
  `id` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `revisionGUID` varchar(36) DEFAULT NULL,
  `postHistoryTypeId` smallint(6) DEFAULT NULL,
  `closeReasonId` smallint(6) DEFAULT NULL,
  `userDisplayName` varchar(32) DEFAULT NULL,
  `text` text CHARACTER SET utf8 COLLATE utf8_bin,
  `comment` text CHARACTER SET utf8 COLLATE utf8_bin,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `posts` (
  `id` int(11) NOT NULL,
  `parentId` int(11) DEFAULT NULL,
  `postTypeId` smallint(6) DEFAULT NULL,
  `ownerUserId` int(11) DEFAULT NULL,
  `acceptedAnswerId` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `closedDate` datetime DEFAULT NULL,
  `communityOwnedDate` datetime DEFAULT NULL,
  `lastEditDate` datetime DEFAULT NULL,
  `lastEditorUserId` int(11) DEFAULT NULL,
  `lastEditorDisplayName` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lastActivityDate` datetime DEFAULT NULL,
  `tags` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `title` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `body` text CHARACTER SET utf8 COLLATE utf8_bin,
  `answerCount` smallint(6) DEFAULT NULL,
  `favoriteCount` int(11) DEFAULT NULL,
  `viewCount` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `commentCount` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `posts_creation_key` (`creationDate`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `postseries` (
  `id` int(11) NOT NULL,
  `postTypeId` smallint(6) DEFAULT NULL,
  `parentId` int(11) DEFAULT NULL,
  `ownerUserId` int(11) DEFAULT NULL,
  `acceptedAnswerId` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `closedDate` datetime DEFAULT NULL,
  `communityOwnedDate` datetime DEFAULT NULL,
  `lastEditDate` datetime DEFAULT NULL,
  `lastEditorUserId` int(11) DEFAULT NULL,
  `lastActivityDate` datetime DEFAULT NULL,
  `answerCount` smallint(6) DEFAULT NULL,
  `favoriteCount` smallint(6) DEFAULT NULL,
  `viewCount` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT NULL,
  `commentCount` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `post_owner_key` (`ownerUserId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `posttags` (
  `postId` int(11) NOT NULL,
  `tagId` int(11) NOT NULL,
  UNIQUE KEY `postTags` (`postId`,`tagId`),
  KEY `ptPost` (`postId`),
  KEY `ptTag` (`tagId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `preseries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_series` (`userid`,`tag`),
  KEY `preseries_user` (`userid`),
  KEY `preseries_tag` (`tag`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `preseries_event` (
  `seriesId` int(11) NOT NULL,
  `tag` char(1) NOT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  `hour` smallint(6) NOT NULL,
  `counter` smallint(6) NOT NULL,
  UNIQUE KEY `preseries_event` (`seriesId`,`tag`,`date`,`hour`),
  KEY `pre_event_date` (`date`),
  KEY `preseries_event_summary` (`seriesId`,`date`),
  KEY `preseries_event_series` (`seriesId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `preseries_sax` (
  `seriesId` int(11) NOT NULL,
  `date` date NOT NULL,
  `sax_string` char(5) NOT NULL,
  UNIQUE KEY `preseries_sax_entry` (`seriesId`,`date`),
  KEY `preseries_sax_word` (`sax_string`) USING HASH
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `sax_entry` (
  `series_id` int(11) NOT NULL,
  `string_id` int(11) NOT NULL,
  `date` date NOT NULL DEFAULT '0000-00-00',
  UNIQUE KEY `se_summary` (`series_id`,`string_id`,`date`),
  KEY `se_series` (`series_id`),
  KEY `se_string` (`string_id`),
  KEY `entry_date` (`date`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `sax_series` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `author_id` int(11) NOT NULL DEFAULT '0',
  `parameters` varchar(16) NOT NULL,
  `tag` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_series` (`author_id`,`parameters`,`tag`),
  KEY `saxauthor_id` (`author_id`),
  KEY `saxparameters` (`parameters`),
  KEY `saxtag` (`tag`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `sax_string` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `string` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `saxstring` (`string`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag` varchar(64) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tags_key` (`tag`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `displayName` varchar(36) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `emailHash` char(32) CHARACTER SET ascii DEFAULT NULL,
  `age` smallint(6) DEFAULT '0',
  `location` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `locationId` int(11) DEFAULT NULL,
  `websiteUrl` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `aboutMe` varchar(5417) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `lastAccessDate` datetime DEFAULT NULL,
  `reputation` int(11) DEFAULT '0',
  `views` int(11) DEFAULT '0',
  `upVotes` int(11) DEFAULT '0',
  `downVotes` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_location_id` (`locationId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `users_answers` (
  `ownerUserId` int(11) DEFAULT NULL,
  `answers_count` int(11) DEFAULT NULL,
  KEY `ua_user` (`ownerUserId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `users_posts` (
  `ownerUserId` int(11) DEFAULT NULL,
  `posts_count` int(11) DEFAULT NULL,
  KEY `up_user` (`ownerUserId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `users_questions` (
  `ownerUserId` int(11) DEFAULT NULL,
  `questions_count` int(11) DEFAULT NULL,
  KEY `uq_user` (`ownerUserId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE `votes` (
  `id` int(11) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `postId` int(11) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `voteTypeId` int(11) DEFAULT NULL,
  `bountyAmount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
