FROM java:8
ADD target/demo-0.0.1-SNAPSHOT.jar planilha.jar
EXPOSE 8088
CMD "java" "-jar" "planilha.jar"
