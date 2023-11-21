
### 基础镜像，使用alpine操作系统，openjkd使用8u201
FROM openjdk:8u201-jdk-alpine3.9

# 设置工作目录
#WORKDIR /app
#VOLUME /tmp
# 复制Spring Boot应用程序的JAR文件到容器中
COPY target/scheduling-0.0.1-SNAPSHOT.jar app.jar

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

#声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /tmp

#启动容器时的进程
ENTRYPOINT ["java","-jar","/app.jar"]

#暴露8080端口
EXPOSE 8080
