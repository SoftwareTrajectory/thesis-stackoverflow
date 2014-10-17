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
d=data.frame(hour=c(0:23),"Jon_Skeet"=skeet,"Marc_Gravell"=gravell,"Darin_Dimitrov"=dimitrov,"Greg_Hewgill"=hewgill)
row.names(d)=paste(c(0:23))
dm = melt(d,id.vars=c("hour"))
p=ggplot(data=dm,aes(x=hour,y=value,fill=variable))+theme_bw()+
  scale_x_continuous("Hour of the day", breaks=c(0:23),labels=c(0:23))+
  ggtitle("Mean activity (posts) per hour") +
  geom_bar(stat = "identity",position="dodge", guide=guide_legend("Contributor",title.position="top"))
p
