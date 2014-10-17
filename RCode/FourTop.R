require(reshape)
require(scales)
require(Cairo)
require(ggplot2)
require(reshape2)
require(plyr)
require(RColorBrewer)
require(grid)
require(gridExtra)
require(lattice)
#
day_activity=function(filename){
 d1=read.table(filename,as.is=T,header=F,sep=",")
 freqs=as.data.frame(d1[,2:25])
 cmeans=colMeans(freqs)
 cmeans
}
skeet=day_activity("../results//Jon_SkeetDAILY_8_3.csv")
hewgill=day_activity("../results//Greg_HewgillDAILY_8_3.csv")
dimitrov=day_activity("../results//Darin_DimitrovDAILY_8_3.csv")
gravell=day_activity("../results//Marc_GravellDAILY_8_3.csv")
balus=day_activity("../results//BalusCDAILY_8_3.csv")
d=data.frame(hour=c(0:23),"Jon_Skeet"=skeet,"Bauke_Scholtz"=balus,
             "Marc_Gravell"=gravell,"Darin_Dimitrov"=dimitrov,"Greg_Hewgill"=hewgill)
row.names(d)=paste(c(0:23))
dm = melt(d,id.vars=c("hour"))
p=ggplot(data=dm,aes(x=hour,y=value,fill=variable))+theme_bw()+
  scale_x_continuous("Hour of the day", breaks=c(0:23),labels=c(0:23))+
  scale_y_continuous("Posts per hour", breaks=c(0:23),labels=c(0:23))+
  ggtitle("Mean activity (posts) per hour") +
  geom_bar(stat = "identity",position="dodge") +
  theme(legend.position="bottom",plot.title=element_text(size=18),
        legend.key.size=unit(0.5, "cm"),axis.ticks.margin=unit(0.5, "cm"),
        axis.title.x=element_text(size=14), axis.title.y=element_text(size=14),
        axis.text.x=element_text(size=10),axis.text.y=element_text(size=10))
p

znorm <- function(ts){
  ts.mean <- mean(ts)
  ts.dev <- sd(ts)
  (ts - ts.mean)/ts.dev
}
dn=data.frame(hour=c(0:23),"Jon_Skeet"=znorm(skeet),"Bauke_Scholtz"=znorm(balus),
              "Marc_Gravell"=znorm(gravell),"Darin_Dimitrov"=znorm(dimitrov),
              "Greg_Hewgill"=znorm(hewgill))
row.names(dn)=paste(c(0:23))
dmn = melt(dn,id.vars=c("hour"))
p1=ggplot(data=dmn,aes(x=hour,y=value,color=variable))+theme_bw()+
  scale_x_continuous("Hour of the day", breaks=c(0:23),labels=c(0:23))+
  ggtitle("Normalized activity (posts) per hour") + geom_line() + 
  theme(legend.position="bottom",plot.title=element_text(size=18),
        legend.key.size=unit(0.5, "cm"),axis.ticks.margin=unit(0.5, "cm"),
        axis.title.x=element_text(size=14), axis.title.y=element_text(size=14),
        axis.text.x=element_text(size=10),axis.text.y=element_text(size=10))
p1

print(arrangeGrob(p,p1,ncol=2))
