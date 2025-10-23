# FROM openjdk:17-jdk-slim
# RUN apt-get update && apt-get install -y maven && apt-get clean

# WORKDIR /app

# COPY pom.xml .
# COPY src ./src

# # # Tạo thư mục logs để ghi log
# # RUN mkdir -p /app/logs && chmod -R 777 /app/logs

# RUN mvn clean package -DskipTests

# EXPOSE 9090

# ENTRYPOINT ["java", "-jar", "target/GentleMonsterBE-0.0.1-SNAPSHOT.war"]

FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/GentleMonsterBE-0.0.1-SNAPSHOT.war GentleMonsterBE-0.0.1-SNAPSHOT.war

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "GentleMonsterBE-0.0.1-SNAPSHOT.war"]
