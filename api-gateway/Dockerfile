# --- 1단계: 빌드 환경 ---
# Gradle과 JDK 21이 포함된 이미지를 사용하여 애플리케이션을 빌드합니다.
FROM gradle:8.5.0-jdk21 AS build

# 작업 디렉터리를 생성합니다.
WORKDIR /home/gradle/src

# build.gradle 파일을 먼저 복사하여 의존성을 캐싱합니다.
COPY build.gradle settings.gradle ./
# 의존성을 다운로드합니다.
RUN gradle build --no-daemon || return 0

# 전체 소스 코드를 복사합니다.
COPY . .

# 애플리케이션을 빌드하고 실행 가능한 JAR 파일을 생성합니다.
RUN gradle build --no-daemon

# --- 2단계: 실행 환경 ---
# 더 가볍고 안전한 JRE(Java 실행 환경) 이미지를 사용합니다.
FROM amazoncorretto:21-alpine-jre

# 작업 디렉터리를 설정합니다.
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 실행 환경으로 복사합니다.
# build/libs/*.jar 패턴을 사용하여 정확한 JAR 파일 이름을 찾습니다.
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# 컨테이너가 시작될 때 실행할 명령을 정의합니다.
ENTRYPOINT ["java", "-jar", "app.jar"]