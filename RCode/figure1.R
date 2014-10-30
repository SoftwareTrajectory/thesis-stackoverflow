# created by seninp 22-10-2012
##
# source("http://bioconductor.org/biocLite.R")
# biocLite(c("Cairo","ggplot2","gridExtra","reshape","scales","RMySQL","xtable", etc))
#require(Cairo)
require(ggplot2)
require(Cairo)
require(gridExtra)
require(reshape)
require(scales)
require(RMySQL)
require(xtable)
require(RColorBrewer)
#
#
# get connected
con <- dbConnect(MySQL(), user="stack", password="stack", 
                 dbname="stack", host="localhost", port=3306)

#
# weekly questions and answers
#

weekly_questions <-dbGetQuery(con, paste("select ",
    "cast((unix_timestamp(creationdate)  / 86400  +3 ) / 7 as UNSIGNED) as week, ",
    "count(*) as questions from posts where postTypeId=1 group by week order by week", sep=""))
weekly_questions[,1]=weekly_questions[,1]-2014
weekly_answers <-dbGetQuery(con, paste("select ",
    "cast((unix_timestamp(creationdate)  / 86400  +3 ) / 7 as UNSIGNED) as week, ",
    "count(*) as answers from posts where postTypeId=2 group by week order by week", sep=""))
weekly_answers[,1]=weekly_answers[,1]-2014

data <- cbind(weekly_questions, weekly_answers$answers)
colnames(data)<-c("week","questions","answers")
d<-melt(data,id.vars="week")

breaks=c(0, as.numeric(difftime("2009-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2010-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2011-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2012-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2012-08-01", "2008-07-31", units="weeks")))
labels=c("2008-07-31","2009-01-01","2010-01-01","2011-01-01","2012-01-01","2012-08-01")

phr <- ggplot(d, aes(x=week, y=value, color=variable)) + geom_line(size=2) + theme_bw() + 
  scale_y_continuous("Count") +
  scale_x_continuous("Weeks since July 31, 2008", limits=c(0,210), breaks=breaks, labels=labels) +
  ggtitle("Weekly questions and answers, StackOverflow") + labs(colour = "Series") +
  theme(plot.title=element_text(size = 24, vjust = 2),
        legend.position = c(0.2, 0.8), # c(0,0) bottom left, c(1,1) top-right.
        legend.background = theme_rect(fill = "white", colour = NA),
        axis.text.x=element_text(size=9, colour = "grey50", angle=45, hjust = 0.9, vjust = 0.9),
        legend.title=element_text(size=14), legend.text=element_text(size=14))
phr


users_registrations <-dbGetQuery(con, paste("SELECT cast((unix_timestamp(CreationDate)  / 86400  +3 ) / 7 as UNSIGNED) as week, ",
  "COUNT(*) as c  FROM users GROUP BY week ORDER BY week", sep=""))
users_registrations[,1]=users_registrations[,1]-2014

colnames(users_registrations)<-c("week","new_users")
d<-melt(users_registrations,id.vars="week")

breaks=c(0, as.numeric(difftime("2009-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2010-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2011-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2012-01-01", "2008-07-31", units="weeks")),
         as.numeric(difftime("2012-08-01", "2008-07-31", units="weeks")))
labels=c("2008-07-31","2009-01-01","2010-01-01","2011-01-01","2012-01-01","2012-08-01")

phr1 <- ggplot(d, aes(x=week, y=value)) + geom_line(size=2,color="cornflowerblue") + theme_bw() + 
  scale_y_continuous("New users count") +
  scale_x_continuous("Weeks since July 31, 2008", limits=c(0,210), breaks=breaks, labels=labels) +
  ggtitle("Weekly new StackOverflow users registrations") +
  theme(plot.title=element_text(size = 24, vjust = 2),
        axis.text.x=element_text(size=9, colour = "grey50", angle=45, hjust = 0.9, vjust = 0.9),
        legend.title=element_text(size=14), legend.text=element_text(size=14))
phr1


Cairo(width = 1500, height = 500, 
      file="figures/stack_overview", 
      type="ps", pointsize=16, 
      bg = "transparent", canvas = "white", units = "px", dpi = 82)
print(arrangeGrob(phr, phr1, ncol=2))
dev.off()