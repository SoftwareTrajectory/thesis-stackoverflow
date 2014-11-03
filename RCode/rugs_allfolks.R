require(ggplot2)
require(Cairo)
require(gridExtra)
require(reshape)
require(scales)
require(RMySQL)
require(xtable)
require(RColorBrewer)

#
# get connected
dat_daily = read.table("../results//DAILY/Jon_SkeetDAILY_8_3.csv",sep=',',as.is=T)
dat_daily=dat_daily[,-1];dat_daily=dat_daily[,1:(length(dat_daily[1,])-1)]

means_daily=data.frame(hour=c(0:23),mean_activity=colMeans(dat_daily))
d_means <- ggplot(means_daily, aes(x=hour, y=mean_activity)) + theme_bw() + 
  geom_line(color="blue",size=1) + ylab("Mean posts activity") +
  scale_x_continuous("Hour of the day",breaks=seq(0,23,3)) + 
  ggtitle("Jon Skeet's daily activity pattern") +
  theme(legend.position="bottom",plot.title=element_text(size=18))
d_means

dat=data.frame(cbind(seq(1,length(dat_daily[,1])*1,by=1),dat_daily))
names(dat)=c("day",paste(c(1:24)))
dm=melt(dat,id.var=c("day"))

rug_daily=ggplot(data=dm,aes(x=variable,y=day,group=day,fill=value))+ 
  ylab("Days from the account registration") + scale_x_discrete("Hour of the day",breaks=seq(0,23,3)) + 
  geom_raster()+theme_bw() + ggtitle("Jon Skeet's daily activity for all time") +
  scale_fill_gradientn(
  colours=c("white","cornflowerblue","darkgoldenrod1","brown3","darkred"),
  name="Posts per hour: ",
  breaks=c(0,10,20,30),
  guide = guide_colorbar(title.theme=element_text(size=14, angle=0),
  title.vjust=1, barheight=0.6, barwidth=6, label.theme=element_text(size=10, angle=0)))+
  theme(legend.position="bottom",plot.title=element_text(size=18))
rug_daily

#
#
weekly_plot = function(dat_weekly,title){
  dat_weekly=dat_weekly[,-1];dat_weekly=dat_weekly[,1:(length(dat_weekly[1,])-1)]
  datw=data.frame(cbind(seq(1,length(dat_weekly[,1])*1,by=1),dat_weekly))
  names(datw)=c("week",paste(c(1:7)))
  dmw=melt(datw,id.var=c("week"))
  rug_weekly=ggplot(data=dmw,aes(x=variable,y=week,group=week,fill=value))+ 
    ylab("Weeks from the account registration") + 
    scale_x_discrete("Day of the week",breaks=seq(1,7,1),labels=c("Mon","Tue","Wed","Thu","Fri","Sat","Sun")) + 
    geom_raster()+theme_bw() + ggtitle(title) +
    scale_fill_gradientn(
      colours=c("white","cornflowerblue","darkgoldenrod1","brown3","darkred"),
      name="Posts per day: ",
      breaks=seq(0,150,by=30),
      guide = guide_colorbar(title.theme=element_text(size=14, angle=0),
      title.vjust=1, barheight=0.6, barwidth=6, label.theme=element_text(size=10, angle=0)))+
    theme(legend.position="bottom",plot.title=element_text(size=18))
  rug_weekly
}

dat_weekly = read.table("../results//WEEKLY/Jon_Skeet_weekly_7_3.csv",sep=',',as.is=T)
p_skeet=weekly_plot(dat_weekly,"Jon Skeet's weekly activity for all time")

dat_weekly = read.table("../results//WEEKLY/Darin_Dimitrov_weekly_7_3.csv",sep=',',as.is=T)
p_dimitrov=weekly_plot(dat_weekly,"Darin Dimitrov's weekly activity for all time")

dat_weekly = read.table("../results//WEEKLY/BalusC_weekly_7_3.csv",sep=',',as.is=T)
p_balus=weekly_plot(dat_weekly,"BalusC' weekly activity for all time")

dat_weekly = read.table("../results//WEEKLY/Hans_Passant_weekly_7_3.csv",sep=',',as.is=T)
p_passant=weekly_plot(dat_weekly,"Hans Passant's weekly activity for all time")

dat_weekly = read.table("../results//WEEKLY/Marc_Gravell_weekly_7_3.csv",sep=',',as.is=T)
p_gravel=weekly_plot(dat_weekly,"Marc Gravell's weekly activity for all time")


grid.arrange(p_skeet, p_dimitrov, p_gravel, p_balus, p_passant, ncol=3)

Cairo(width = 1600, height = 1200, 
      file="figures/rugs_all_weekly", 
      type="ps", pointsize=12, 
      bg = "transparent", canvas = "white", units = "px", dpi = 82)
grid.arrange(p_skeet, p_dimitrov, p_gravel, p_balus, p_passant, ncol=3)
dev.off()
#
#
#
#

daily_plot = function(dat_daily,title){
  dat_daily=dat_daily[,-1];dat_daily=dat_daily[,1:(length(dat_daily[1,])-1)]
  dat=data.frame(cbind(seq(1,length(dat_daily[,1])*1,by=1),dat_daily))
  names(dat)=c("day",paste(c(1:24)))
  dm=melt(dat,id.var=c("day"))
  rug_daily=ggplot(data=dm,aes(x=variable,y=day,group=day,fill=value))+ 
    ylab("Days from the account registration") + scale_x_discrete("Hour of the day",breaks=seq(0,23,3)) + 
    geom_raster()+theme_bw() + ggtitle(title) +
    scale_fill_gradientn(
      colours=c("white","cornflowerblue","darkgoldenrod1","brown3","darkred"),
      name="Posts per hour: ",
      breaks=seq(0,30,by=10),
      guide = guide_colorbar(title.theme=element_text(size=14, angle=0),
      title.vjust=1, barheight=0.6, barwidth=6, label.theme=element_text(size=10, angle=0)))+
    theme(legend.position="bottom",plot.title=element_text(size=18))
  rug_daily
}

dat_weekly = read.table("../results//DAILY/Jon_SkeetDAILY_8_3.csv",sep=',',as.is=T)
p_skeet=daily_plot(dat_weekly,"Jon Skeet's daily activity for all time")

dat_weekly = read.table("../results//DAILY//Darin_DimitrovDAILY_8_3.csv",sep=',',as.is=T)
p_dimitrov=daily_plot(dat_weekly,"Darin Dimitrov's daily activity for all time")

dat_weekly = read.table("../results//DAILY/BalusCDAILY_8_3.csv",sep=',',as.is=T)
p_balus=daily_plot(dat_weekly,"BalusC' daily activity for all time")

dat_weekly = read.table("../results//DAILY/Hans_Passant_daily_8_3.csv",sep=',',as.is=T)
p_passant=daily_plot(dat_weekly,"Hans Passant's daily activity for all time")

dat_weekly = read.table("../results//DAILY/Marc_GravellDAILY_8_3.csv",sep=',',as.is=T)
p_gravel=daily_plot(dat_weekly,"Marc Gravell's daily activity for all time")


grid.arrange(p_skeet, p_dimitrov, p_gravel, p_balus, p_passant, ncol=3)

Cairo(width = 1600, height = 1200, 
      file="figures/rugs_all_daily", 
      type="ps", pointsize=12, 
      bg = "transparent", canvas = "white", units = "px", dpi = 82)
grid.arrange(p_skeet, p_dimitrov, p_gravel, p_balus, p_passant, ncol=3)
dev.off()