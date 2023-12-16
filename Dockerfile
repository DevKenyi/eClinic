FROM openjdk:19-oraclelinux8
LABEL maintaner="dev eclinic docker"
ADD target/eClinic001-0.0.1-SNAPSHOT.jar /eClinic-dockerImage.jar
ENTRYPOINT ["java","-jar","eClinc-dockerImage.jar"]
