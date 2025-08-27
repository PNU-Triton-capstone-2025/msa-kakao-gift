# --- 1단계: 빌드 환경 ---
FROM gradle:8.5.0-jdk21 AS build
WORKDIR /home/gradle/src
COPY build.gradle settings.gradle ./
RUN gradle build --no-daemon || return 0
COPY . .

# gradle build 명령어에 '-x test' 옵션을 추가하여 테스트를 건너뜁니다.
RUN gradle build --no-daemon -x test

# --- 2단계: 실행 환경 ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]