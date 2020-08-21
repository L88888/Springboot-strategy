#通过该Dockerfile文件制作出来的镜像为精简版镜像，不包含jre,体积小
#获取基础镜像sailing.com/alpine-base:1.0.0
FROM sailing.com/debian:1.0.2
VOLUME /usr/lib/jvm/default-jvm
VOLUME /DIPFB/application
VOLUME /tmp
ENV JAVA_HOME /usr/lib/jvm/default-jvm
ENV PATH ${PATH}:${JAVA_HOME}/bin
ADD server-userphoto-async-DIPFB-1.0.0.jar /DIPFB/application/app.jar
#该端口为自定义端口,与配置文件中的server.port值一致
EXPOSE 2038
#jar 运行命令 java -Djava.security.egd -jar /app.jar, -Djava.security.egd表示自启动的意思
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Xms2048m","-Xmx2048m","-jar","/DIPFB/application/app.jar"]