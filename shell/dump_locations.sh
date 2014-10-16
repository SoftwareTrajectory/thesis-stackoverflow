#!/bin/bash

DBUSER=stack
DBPASS=stack
DBNAME=stack
DATE=$(date +%Y%m%d)
LOCATION=.
TABLES="locations users preseries preseries_event"       
 
#Dump the database to a .sql file
mysqldump -u${DBUSER} -p${DBPASS} -R ${DBNAME} ${TABLES}> ${LOCATION}/${DBNAME}_${DATE}-preseries.sql
 
#Gzip the sql file
cd ${LOCATION}
tar -zcf ${DBNAME}_${DATE}-preseries.tgz ${DBNAME}_${DATE}-preseries.sql

exit
