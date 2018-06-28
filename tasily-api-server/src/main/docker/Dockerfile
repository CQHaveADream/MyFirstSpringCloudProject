#Docker 使用 Dockerfile 文件格式来指定 image 层

#FROM指令必须指定且需要在Dockerfile其他指令的前面，
#指定的基础image可以是官方远程仓库中的，也可以位于本地仓库。后续的指令都依赖于该指令指定的image。当在同一个Dockerfile中建立多个镜像时，可以使用多个FROM指令
FROM frolvlad/alpine-oraclejdk8:slim

#指定了临时文件目录为/tmp,其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
#/tmp目录用来持久化到 Docker 数据文件夹，因为 Spring Boot 使用的内嵌 Tomcat 容器默认使用/tmp作为工作目录
VOLUME /tmp

#项目的 jar 文件作为 “app.jar” 添加到容器
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar

#RUN bash -c 'touch /app.jar'
#执行项目 app.jar。为了缩短 Tomcat 启动时间，添加一个系统属性指向 “/dev/urandom” 作为 Entropy Source
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

EXPOSE 8761