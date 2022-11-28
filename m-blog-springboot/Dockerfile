FROM java:8
EXPOSE 8080

CMD ["--server.port=8080"]

ADD *.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
