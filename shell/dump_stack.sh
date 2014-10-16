#!/bin/bash

DBUSER=stack
DBPASS=stack
DBNAME=stack
DATE=$(date +%Y%m%d)
LOCATION=/media/Stock
TABLES="commentseries locations postseries preseries preseries_event preseries_sax sax_entry sax_series sax_string tags users"
 
#Dump the database to a .sql file
mysqldump -u${DBUSER} -p${DBPASS} -R ${DBNAME} ${TABLES}> ${LOCATION}/${DBNAME}_${DATE}.sql
 
#Gzip the sql file
cd ${LOCATION}
tar -zcf ${DBNAME}_${DATE}.tgz ${DBNAME}_${DATE}.sql

exit
